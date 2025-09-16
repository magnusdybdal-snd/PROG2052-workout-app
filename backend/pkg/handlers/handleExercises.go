package handlers

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type Data struct {
	Data []struct {
		GifUrl string `json:"gifUrl"`
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
			getExercises(db)(w,r)
		case http.MethodPost:
		default:
			utils.HandleError(w,
				http.StatusMethodNotAllowed,
				fmt.Errorf("method not allowed"),
				ErrMsgNotAllowed)
			return
		}
	}
}

/*
handler for GET /exercises

returns all exercises in database
*/
func getExercises(db *mongo.Client) http.HandlerFunc {
	log.Println("Handler: GET exercises found")
	type Exercises struct {
		Name string `bson:"name" json:"name"`
	}

	coll := db.Database("TrainingApp").Collection("exercises")

	return func(w http.ResponseWriter, r *http.Request) {
        ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
        defer cancel()

        cursor, err := coll.Find(ctx, bson.M{})
        if err != nil {
            utils.HandleError(w, http.StatusInternalServerError, err, "Failed to fetch exercises")
            return
        }
        defer cursor.Close(ctx)

        var exercises []Exercises
        if err := cursor.All(ctx, &exercises); err != nil {
            utils.HandleError(w, http.StatusInternalServerError, err, "Failed to decode exercises")
            return
        }
		if len(exercises) == 0 {
			utils.HandleError(w,http.StatusInternalServerError, err, "no exercises found")
			return
		}

        utils.Encode(w, r, http.StatusOK, exercises)
	}
}
