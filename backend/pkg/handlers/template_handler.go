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
*/

// Handles template/ and template/{templateId}
func HandleTemplate(serv *services.TemplateService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		switch r.Method {
		case http.MethodGet:
			data, err := serv.GetAllTemplates(ctx)
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
			id, err := serv.PostOneTemplate(context.TODO(), payload)
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

func GetOneTemplates(serv *services.TemplateService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
		id := r.PathValue("templateId")
		if id == "" {
			utils.HandleError(w, http.StatusBadRequest, fmt.Errorf("bad id"), utils.ErrMsgBadRequest)
			return
		}
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetOneTemplate(ctx, id)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		utils.Encode(w, http.StatusOK, data)
	}
}
