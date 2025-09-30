package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type TemplateService struct {
	RepoTempl *repository.TemplateRepository
	RepoExer *repository.ExerciseRepository
}

func NewTemplateService(
	rTempl *repository.TemplateRepository, 
	rExer *repository.ExerciseRepository,
) *TemplateService {
	return &TemplateService{
		RepoTempl: rTempl,
		RepoExer: rExer,
	}
}

func (s *TemplateService) GetAllTemplates(ctx context.Context) ([]domain.Template, error) {
	return s.RepoTempl.GetAllTemplates(ctx)
}

func (s *TemplateService) GetOneTemplate(ctx context.Context,id string, include bool) (interface{}, error) {
	templ,err := s.RepoTempl.GetOneTemplate(ctx,id)
	if err != nil {
		return nil, err
	}
	if !include {
		return templ, nil
	}

	var expandedTempl domain.ExpandedTemplate
	expandedTempl.TemplateId = templ.TemplateId
	expandedTempl.Name = templ.Name

	for _, et := range templ.Exercises {
		ex, err := s.RepoExer.GetOneExercise(ctx,et.ExerciseId)
		if err != nil {
			return nil, err
		}
		expandedTempl.Exercises = append(expandedTempl.Exercises, domain.ExpandedExerciseTemplate{
			Exercise: ex,
			Set: et.Set,
		})
	}

	return expandedTempl,nil
}

func (s *TemplateService) PostOneTemplate(ctx context.Context, payload interface{}) (string, error) {
	return "test", nil
}
