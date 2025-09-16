package handlers

import (
	"fmt"
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func HandleExercises(db *mongo.Client) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w,
				http.StatusMethodNotAllowed,
				fmt.Errorf("method not allowed"),
				"Method not allowed")
		} else {
			w.Write([]byte("exercises"))
		}
	}
}
