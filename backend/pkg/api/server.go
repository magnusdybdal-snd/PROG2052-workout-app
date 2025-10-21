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

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
)

func newServer(
	exerciseService domain.ExerciseService,
	templateService *services.TemplateService,
	SessionService domain.SessionService,
) http.Handler {
	mux := http.NewServeMux()
	addRoutes(
		mux, 
		exerciseService,
		templateService,
		SessionService,
	)

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
		return err
	}

	// Starting up repositories
	exerciseRepo := &repository.ExerciseRepository{
		Coll: mongoDB.Database("TrainingApp").Collection("exercises"),
	}
	templateRepo := &repository.TemplateRepository{
		Coll: mongoDB.Database("TrainingApp").Collection("templates"),
	}
	sessionRepo := &repository.SessionRepository{
		Coll: mongoDB.Database("TrainingApp").Collection("sessions"),
	}

	// Starting up Services
	exerciseService := &services.ExerciseServiceImpl{
		Repo: exerciseRepo,
	}
	
	templateService := services.NewTemplateService(templateRepo, exerciseRepo)

	SessionService := &services.SessionServiceImpl{
		Repo: sessionRepo,
		RepoExer: exerciseRepo,
	}
	
	// Setting up routes and starting http server
	srv := newServer(exerciseService,  templateService, SessionService)
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

	db.CloseDB(mongoDB)
	return nil
}
