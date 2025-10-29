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
	mux.Handle(API_ROUTE,handlers.HandleHome())

	/*
		Endpoints
	*/
	// GET /exercises
	mux.Handle(EXERCISES_ROUTE,handlers.HandleExercises(container.ExerciseService))
	// GET /exercises/{exerciseId}
	mux.Handle(EXERCISES_ID_ROUTE,handlers.HandleOneExercise(container.ExerciseService))

	// GET, POST /sessions
	mux.Handle(SESSIONS_ROUTE, handlers.HandleSession(container.SessionService))
	// DELETE /sessions
	mux.Handle(SESSIONS_ID_ROUTE, handlers.HandleOneSession(container.SessionService))

	// GET, POST /templates
	mux.Handle(TEMPLATES_ROUTE, handlers.HandleTemplate(container.TemplateService))
	// GET /templates/{templateId}
	mux.Handle(TEMPLATES_ID_ROUTE,handlers.HandleOneTemplate(container.TemplateService))

	// POST /auth/google
	mux.Handle(AUTH_ROUTE, handlers.HandleAuth())

	mux.Handle(API_ROUTE + "/helloAuth", AuthenticateUser(cfg,handlers.HelloAuth()))

	// Media
	mux.Handle(MEDIA_ROUTE,http.StripPrefix(MEDIA_ROUTE, handlers.HandleMedia()))

	// other 
	mux.HandleFunc("/",handlers.NotFound())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
