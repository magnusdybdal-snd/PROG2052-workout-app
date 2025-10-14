package api

import (
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
)

func addRoutes(
	mux *http.ServeMux, 
	exerciseService *services.ExerciseService,
	templateService *services.TemplateService,
	sessionService *services.SessionService,
) {
	// Home route
	mux.Handle(API_ROUTE,handlers.HandleHome())

	/*
		Endpoints
	*/
	// GET /exercises
	mux.Handle(EXERCISES_ROUTE,handlers.GetAllExercises(exerciseService))
	// GET /exercises/{exerciseId}
	mux.Handle(EXERCISES_ID_ROUTE,handlers.GetOneExercise(exerciseService))

	// GET, POST /sessions
	mux.Handle(SESSIONS_ROUTE, handlers.HandleSession(sessionService))
	// DELETE /sessions
	mux.Handle(SESSIONS_ID_ROUTE, handlers.DeleteSession(sessionService))

	// GET, POST /templates
	mux.Handle(TEMPLATES_ROUTE, handlers.HandleTemplate(templateService))
	// GET /templates/{templateId}
	mux.Handle(TEMPLATES_ID_ROUTE,handlers.GetOneTemplate(templateService))

	// Media
	mux.Handle(MEDIA_ROUTE,http.StripPrefix(MEDIA_ROUTE, http.FileServer(http.Dir("assets/exercises"))))

	// other 
	mux.HandleFunc("/",handlers.NotFound())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
