package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type TemplateService struct {
	Repo *repository.TemplateRepository
}

func (s *TemplateService) GetAllTemplates(ctx context.Context) ([]domain.Template, error) {
	return s.Repo.GetAllTemplates(ctx)
}

func (s *TemplateService) GetOneTemplate(ctx context.Context,id string) (domain.Template, error) {
	return s.Repo.GetOneTemplate(ctx,id)
}

func (s *TemplateService) PostOneTemplate(ctx context.Context, payload interface{}) (string, error) {
	return "test", nil
}
