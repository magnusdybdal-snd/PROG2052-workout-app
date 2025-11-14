package handlers

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
)

/*
GET  /exercises      -> get all
GET  /exercises/{id} -> get one
*/

/*
handler for GET /exercises
returns all exercises in database
*/
func HandleExercises(serv domain.ExerciseService, url string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
		// As of now is to provide the whole exercises list
		limit := utils.ParseLimit(r,1500)

		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetAll(ctx,limit, url)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}

		utils.Encode(w, http.StatusOK, data)
	}
}

func HandleOneExercise(serv domain.ExerciseService, url string) http.HandlerFunc {
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

		data, err := serv.GetOne(ctx, id, url)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		utils.Encode(w, http.StatusOK, data)
	}
}
