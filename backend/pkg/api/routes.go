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
	mux.HandleFunc(EXERCISES_ROUTE,handlers.HandleExercises(db))

	// TODO: fix these
	mux.HandleFunc("/",handleHome())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
}
