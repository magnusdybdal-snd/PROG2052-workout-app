package handlers

import (
	"fmt"
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type Data struct {
	Data []struct {    
		GifUrl string  `json:"gifUrl"`
	} `json:"data"`
}

/*
	GET  /exercises      -> get all
	GET  /exercises/{id} -> get one 
	POST /exercises      -> create new
*/
func HandleExercises(db *mongo.Client) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			getExercises(db)
		case http.MethodPost:
		default:
			utils.HandleError(w,
				http.StatusMethodNotAllowed,
				fmt.Errorf("method not allowed"),
				ErrMsgNotAllowed)
		}
	}
}

/*
	handler for GET /exercises 
	
	returns all exercises in database 
*/
func getExercises(db *mongo.Client) http.HandlerFunc {
	return func (w http.ResponseWriter, r *http.Request) {
		
	}
}
