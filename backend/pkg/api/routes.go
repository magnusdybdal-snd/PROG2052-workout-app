package api

import (
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func addRoutes(mux *http.ServeMux, db *mongo.Client) {
	// Todo: make info page for this
	mux.HandleFunc(API_ROUTE,func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("api page"))
	})

	// exercieses endpoint
	mux.HandleFunc(EXERCISES_ROUTE,handlers.GetAllExercises(db))

	// TODO: remove this, only for testing
	mux.HandleFunc("/static", handlers.HandlePng("benchPress.png"))

	// Serve all the images
	// TODO: fix the naming when opening file
	mux.Handle(MEDIA_ROUTE,http.StripPrefix(MEDIA_ROUTE, http.FileServer(http.Dir("assets/exercises"))))

	// TODO: fix these
	mux.HandleFunc("/",handleHome())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
