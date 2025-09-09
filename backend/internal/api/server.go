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
)

func newServer() http.Handler {
	mux := http.NewServeMux()
	addRoutes(mux)

	var handler http.Handler = mux
	// handler = middleware1(handler)

	return handler
}

func addRoutes(mux *http.ServeMux) {
	mux.HandleFunc(API_ROUTE,func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("api page"))
	})
	mux.HandleFunc("/about",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("about page"))
	})
	mux.HandleFunc("/",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("Index page"))
	})
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
	
}

func Run(ctx context.Context, w io.Writer, args []string) error {
	// Context with cancel on interupt
	ctx, cancel := signal.NotifyContext(ctx, os.Interrupt)
	defer cancel()

	// load .env file
	cfg := LoadConfig()

	srv := newServer()
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
	return nil
}
