package api

import (
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/handlers"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func addRoutes(mux *http.ServeMux, db *mongo.Client) {
	mux.HandleFunc(API_ROUTE,func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("api page"))
	})
	mux.HandleFunc("/about",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("about page"))
	})
	mux.HandleFunc(API_ROUTE + "/exercises",handlers.HandleExercises(db))
	mux.HandleFunc("/",handleHome())
	mux.HandleFunc("/admin",func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("admin page"))
	})
	
}
