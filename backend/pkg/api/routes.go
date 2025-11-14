package api

import (
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/di"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
)

func addRoutes(
	mux *http.ServeMux,
	cfg *config.Config,
	container *di.ServiceContainer,
) {
	// Home route
	mux.Handle(API_ROUTE, handlers.HandleHome())

	/*
		Endpoints
	*/
	// GET /exercises
	mux.Handle(EXERCISES_ROUTE, handlers.HandleExercises(container.ExerciseService, cfg.HOST_URL + MEDIA_ROUTE))
	// GET /exercises/{exerciseId}
	mux.Handle(EXERCISES_ID_ROUTE, handlers.HandleOneExercise(container.ExerciseService, cfg.HOST_URL + MEDIA_ROUTE))

	// GET, POST /sessions
	mux.Handle(SESSIONS_ROUTE, AuthenticateUser(cfg, handlers.HandleSession(container.SessionService)))
	// DELETE /sessions
	mux.Handle(SESSIONS_ID_ROUTE, AuthenticateUser(cfg, handlers.HandleOneSession(container.SessionService)))

	// GET, POST /templates
	mux.Handle(TEMPLATES_ROUTE, AuthenticateUser(cfg, handlers.HandleTemplate(container.TemplateService)))
	// GET /templates/{templateId}
	mux.Handle(TEMPLATES_ID_ROUTE, AuthenticateUser(cfg, handlers.HandleOneTemplate(container.TemplateService)))

	// POST /auth/google
	mux.Handle(AUTH_ROUTE, handlers.HandleAuth(cfg))

	// Media
	mux.Handle(MEDIA_ROUTE, http.StripPrefix(MEDIA_ROUTE, handlers.HandleMedia()))

	// other
	mux.HandleFunc("/", handlers.NotFound())
	mux.HandleFunc("/admin", func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
