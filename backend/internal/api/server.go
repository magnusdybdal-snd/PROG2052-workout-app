package api

import (
	"context"
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"sync"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/internal/db"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func newServer(db *mongo.Client) http.Handler {
	mux := http.NewServeMux()
	addRoutes(mux,db)

	middleware := newMiddleware() // top level middleware
	var handler http.Handler = mux
	handler = middleware(handler)

	return handler
}



func Run(ctx context.Context, w io.Writer, args []string) error {
	// Context with cancel on interupt
	ctx, cancel := signal.NotifyContext(ctx, os.Interrupt)
	defer cancel()

	// load .env file
	cfg := LoadConfig()

	// Connect to the database
	mongoDB, err := db.InitDB(cfg.UriDB)
	if err != nil {
		panic(err)
	}

	srv := newServer(mongoDB)
	httpServer := &http.Server {
		Addr: net.JoinHostPort(cfg.Host, cfg.Port),
		Handler: srv,
	}

	go func() {
		log.Printf("Listening on %s\n", httpServer.Addr)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			fmt.Fprintf(os.Stderr,"Error listening and serving: %s\n", err)
		}
		fmt.Printf("\n")
		log.Printf("Closing server\n")
	}()

	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		<-ctx.Done()
		shutDownCtx := context.Background()
		shutDownCtx, cancel := context.WithTimeout(shutDownCtx,10 * time.Second)
		defer cancel()
		if err := httpServer.Shutdown(shutDownCtx); err != nil {
			fmt.Fprintf(os.Stderr,"error shutting down http server: %s\n", err)
		}
	}()
	wg.Wait()

	db.CloseDB(mongoDB)
	return nil
}
