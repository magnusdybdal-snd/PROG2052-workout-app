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

/*
HandleSession()
GET /sessions - retrieves all sessions
POST /session - Insert one session
*/
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
				utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
				return
			}
			utils.Encode(w, http.StatusOK, map[string]string{
				"id":      id,
				"message": "session created successfully",
			})

		// default case
		default:
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
	}
}

/*
Delete one session on ID
*/
func DeleteSession(serv *services.SessionService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodDelete {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}

		id := r.PathValue("sessionId")
		if id == "" {
			utils.HandleError(w, http.StatusBadRequest, fmt.Errorf("bad id"), utils.ErrMsgBadRequest)
			return
		}
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()
		result, err := serv.DeleteSession(ctx, id)
		if err != nil {
			utils.HandleError(w,http.StatusInternalServerError,err,utils.ErrMsgInternal)
			return
		}
		utils.Encode(w, http.StatusOK,map[string]string {
			"id": result,
			"message": "successfuly deleted document on id",
		})
	}
}
