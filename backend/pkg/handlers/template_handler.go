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
GET ALL TEMPLATES
GET ONE TEMPLATE
POST ONE TEMPLATE
POST LIST TEMPLATE
DELETE ONE TEMPLATE
*/

/*
GET /template
POST /template
*/
func HandleTemplate(serv *services.TemplateService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		switch r.Method {
		case http.MethodGet:
			include := utils.ParseInclude(r, "exercises")
			data, err := serv.GetAllTemplates(ctx, include)
			if err != nil {
				utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
				return
			}
			utils.Encode(w, http.StatusOK, data)

		case http.MethodPost:
			payload, problems, err := utils.DecodeValid[*domain.Template](r)
			if err != nil {
				if problems != nil {
					utils.Encode(w, http.StatusBadRequest, problems)
					return
				}
				utils.HandleError(w, http.StatusBadRequest, err, utils.ErrMsgBadRequest)
				return
			}
			id, err := serv.PostOneTemplate(ctx, payload)
			if err != nil {
				utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
				return
			}
			utils.Encode(w, http.StatusOK, map[string]string{
				"id":      id,
				"message": "template created successfully",
			})
		default:
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
	}
}

/*
GET /template/{templateId}
DELETE /template/{templateId}
*/
func HandleOneTemplate(serv *services.TemplateService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		id := r.PathValue("templateId")
		if id == "" {
			utils.HandleError(w, http.StatusBadRequest, fmt.Errorf("bad id"), utils.ErrMsgBadRequest)
			return
		}

		switch r.Method {
		case http.MethodGet:
			include := utils.ParseInclude(r, "exercises")
			data, err := serv.GetOneTemplate(ctx, id, include)
			if err != nil {
				utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
				return
			}
			utils.Encode(w, http.StatusOK, data)
		case http.MethodDelete:
			result, err := serv.DeleteTemplate(ctx, id)
			if err != nil {
				utils.HandleError(w, http.StatusInternalServerError, err, err.Error())
				return
			}
			utils.Encode(w, http.StatusOK, map[string]string{
				"id":      result,
				"message": "successfuly deleted document on id",
			})
		default:
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
	}
}
