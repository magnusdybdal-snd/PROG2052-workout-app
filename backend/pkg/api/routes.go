package api

import (
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func addRoutes(mux *http.ServeMux, db *mongo.Client) {
	// Todo: make info page for this
	mux.Handle(API_ROUTE,handlers.HandleHome())
	/*
		APPLICATION ENDPOINTS
	*/
	mux.HandleFunc(EXERCISES_ROUTE,handlers.GetAllExercises(db))
	mux.HandleFunc(EXERCISES_ID_ROUTE,handlers.GetOneExercise(db))
	mux.HandleFunc(TEMPLATES_ROUTE, handlers.GetAllTemplates(db))
	mux.HandleFunc(TEMPLATES_ID_ROUTE,handlers.GetOneTemplates(db))

	/*
		HTTP FILE SERVER
	*/
	// TODO: fix the naming when opening file
	mux.Handle(MEDIA_ROUTE,http.StripPrefix(MEDIA_ROUTE, http.FileServer(http.Dir("assets/exercises"))))

	// TODO: fix these
	mux.HandleFunc("/",handlers.NotFound())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
