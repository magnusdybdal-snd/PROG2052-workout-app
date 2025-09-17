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

/*
GET  /exercises      -> get all
GET  /exercises/{id} -> get one
POST /exercises      -> create new
*/


/*
handler for GET /exercises
returns all exercises in database
*/
func GetAllExercises(db *mongo.Client) http.HandlerFunc {
	log.Println("Handler: GET exercises found")

	type Exercises struct {
		Id               string   `bson:"exerciseId" json:"exerciseId"`
		Name             string   `bson:"name" json:"name"`
		TargetMuscles    []string   `bson:"targetMuscles" json:"targetMuscles"`
		BodyParts        []string   `bson:"bodyParts" json:"bodyParts"`
		Equipments       []string   `bson:"equipments" json:"equipments"`
		SecondaryMuscles []string   `bson:"secondaryMuscles" json:"secondaryMuscles"`
		GifUrl           string   `bson:"gifUrl" json:"gifUrl"`
		Instructions     []string `bson:"instructions" json:"instructions"`
	}

	coll := db.Database("TrainingApp").Collection("exercises")

	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusInternalServerError, fmt.Errorf("no method"), ErrMsgBadRequest)
			return
		}

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
			utils.HandleError(w, http.StatusInternalServerError, err, "no exercises found")
			return
		}

		utils.Encode(w, r, http.StatusOK, exercises)
	}
}
