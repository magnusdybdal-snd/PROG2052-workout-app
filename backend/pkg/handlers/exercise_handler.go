package handlers

import (
	"context"
	"fmt"
	"net/http"
	"time"

	dbpkg "gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
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
	coll := db.Database("TrainingApp").Collection("exercises")

	serv := &services.ExerciseService {
		Repo: &dbpkg.Repositoty[domain.Exercises] {
			Coll: coll,
		},
	}
	
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("no method"), utils.ErrMsgNotAllowed)
			return
		}

		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetAll(ctx)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}

		utils.Encode(w, http.StatusOK, data)
	}
}

func GetOneExercise(database *mongo.Client) http.HandlerFunc {
	coll := database.Database("TrainingApp").Collection("exercises")
	serv := &services.ExerciseService {
		Repo: &dbpkg.Repositoty[domain.Exercises] {
			Coll: coll,
		},
	}

	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("no method"), utils.ErrMsgNotAllowed)
			return
		}

		id := r.PathValue("exerciseId")
		if id == "" {
			utils.HandleError(w, http.StatusBadRequest, fmt.Errorf("no id"), utils.ErrMsgBadRequest)
			return
		}
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetOne(ctx, id)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		utils.Encode(w, http.StatusOK, data)
	}
}
