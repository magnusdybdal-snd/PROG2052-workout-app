package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
)

type TemplateService struct {
	Repo *db.Repositoty[domain.Template]
}

func (s *TemplateService) GetAllTemplates(ctx context.Context) ([]domain.Template, error) {
	return s.Repo.GetAll(ctx)
}

func (s *TemplateService) GetOneTemplate(ctx context.Context,id string) (domain.Template, error) {
	return s.Repo.GetOne(ctx,bson.M{"templateId":id})
}

func (s *TemplateService) PostOneTemplate(ctx context.Context, payload interface{}) (string, error) {
	return "test", nil
}
