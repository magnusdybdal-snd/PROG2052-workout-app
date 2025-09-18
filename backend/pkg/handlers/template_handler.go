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
GET ALL TEMPLATES
GET ONE TEMPLATE
POST ONE TEMPLATE
POST LIST TEMPLATE
*/

func GetAllTemplates(db *mongo.Client) http.HandlerFunc {
	coll := db.Database("TrainingApp").Collection("templates")

	serv := &services.TemplateService{
		Repo: &dbpkg.Repositoty[domain.Template]{
			Coll: coll,
		},
	}
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}

		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		data, err := serv.GetAllTemplates(ctx)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		utils.Encode(w, http.StatusOK, data)
	}
}

func GetOneTemplates(db *mongo.Client) http.HandlerFunc {
	coll := db.Database("TrainingApp").Collection("templates")

	serv := &services.TemplateService{
		Repo: &dbpkg.Repositoty[domain.Template]{
			Coll: coll,
		},
	}
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
		utils.Encode(w,http.StatusOK,data)
	}
}
