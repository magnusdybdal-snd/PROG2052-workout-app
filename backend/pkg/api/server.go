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

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/di"
)

func newServer(
	cfg *config.Config,
	container *di.ServiceContainer,
) http.Handler {
	mux := http.NewServeMux()
	addRoutes(
		mux, 
		cfg,
		container,
	)

	var handler http.Handler = mux

	handler = CorsMiddleware()(handler) 
	handler = LoggingMiddleware(container.Logger)(handler) 

	return handler
}

func Run(ctx context.Context, w io.Writer, args []string) error {
	// Context with cancel on interupt
	ctx, cancel := signal.NotifyContext(ctx, os.Interrupt)
	defer cancel()

	// load .env file
	cfg := config.LoadConfig()

	// Initilize services
	container, err := di.NewContainer(cfg)
	if err != nil {
		return err
	}
	defer container.Logger.Sync()
	
	// Setting up routes and starting http server
	srv := newServer(cfg,container) // injecting services
	httpServer := &http.Server{
		Addr:    net.JoinHostPort(cfg.Host, cfg.Port),
		Handler: srv,
	}

	go func() {
		log.Printf("Listening on %s\n", httpServer.Addr)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			fmt.Fprintf(os.Stderr, "Error listening and serving: %s\n", err)
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
		shutDownCtx, cancel := context.WithTimeout(shutDownCtx, 10*time.Second)
		defer cancel()
		if err := httpServer.Shutdown(shutDownCtx); err != nil {
			fmt.Fprintf(os.Stderr, "error shutting down http server: %s\n", err)
		}
	}()
	wg.Wait()

	db.CloseDB(container.DB)
	return nil
}
