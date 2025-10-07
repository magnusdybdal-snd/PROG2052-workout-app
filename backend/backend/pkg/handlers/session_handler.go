package handlers

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
)

// Get all sessions
func HandleSession(serv *services.SessionService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		switch r.Method {
		// GET /sessions
		case http.MethodGet:
			include := utils.ParseInclude(r, "exercises")
			data, err := serv.GetAllSession(ctx, include)
			if err != nil {
				utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
				return
			}
			utils.Encode(w, http.StatusOK, data)

		// POST /sessions
		case http.MethodPost:
			payload, problems, err := utils.DecodeValid[*domain.Session](r)
			if err != nil {
				if problems != nil {
					utils.Encode(w, http.StatusBadRequest, problems)
					return
				}
				utils.HandleError(w, http.StatusBadRequest, err, utils.ErrMsgBadRequest)
				return
			}
			id, err := serv.PostSession(ctx, payload)
			if err != nil {
				utils.HandleError(w,http.StatusInternalServerError,err, utils.ErrMsgInternal)
				return
			}
			utils.Encode(w,http.StatusOK,map[string]string{
				"id": id,
				"message": "session created successfully",
			})

		// default case
		default:
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
	}
}
