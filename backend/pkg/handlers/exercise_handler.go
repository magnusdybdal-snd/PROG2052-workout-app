package handlers

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
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
func GetAllExercises(serv *services.ExerciseService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
		limit := utils.ParseLimit(r,100)

		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetAllExercises(ctx,limit)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}

		utils.Encode(w, http.StatusOK, data)
	}
}

func GetOneExercise(serv *services.ExerciseService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}

		id := r.PathValue("exerciseId")
		if id == "" {
			utils.HandleError(w, http.StatusBadRequest, fmt.Errorf("bad id"), utils.ErrMsgBadRequest)
			return
		}
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetOneExercise(ctx, id)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		utils.Encode(w, http.StatusOK, data)
	}
}
