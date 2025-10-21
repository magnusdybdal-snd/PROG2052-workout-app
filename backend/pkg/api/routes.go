package api

import (
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
)

func addRoutes(
	mux *http.ServeMux, 
	exerciseService domain.ExerciseService,
	templateService domain.TemplateService,
	sessionService domain.SessionService,
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
	mux.Handle(SESSIONS_ID_ROUTE, handlers.HandleOneSession(sessionService))

	// GET, POST /templates
	mux.Handle(TEMPLATES_ROUTE, handlers.HandleTemplate(templateService))
	// GET /templates/{templateId}
	mux.Handle(TEMPLATES_ID_ROUTE,handlers.HandleOneTemplate(templateService))

	// Media
	mux.Handle(MEDIA_ROUTE,http.StripPrefix(MEDIA_ROUTE, http.FileServer(http.Dir("assets/exercises"))))

	// other 
	mux.HandleFunc("/",handlers.NotFound())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
