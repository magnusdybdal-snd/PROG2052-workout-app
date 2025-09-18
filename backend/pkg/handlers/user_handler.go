package handlers

import (
	"fmt"
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func GetAllUsers(db *mongo.Client) http.HandlerFunc {
	//coll := db.Database("TrainingApp").Collection("users")

	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("no method"), utils.ErrMsgBadRequest)
			return
		}

		
	}
}
