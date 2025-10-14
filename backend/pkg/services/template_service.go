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

func (s *TemplateService) GetAllTemplates(ctx context.Context, include bool) (interface{}, error) {
	templ, err := s.RepoTempl.GetAllTemplates(ctx)
	if err != nil {
		return nil, err
	}
	if !include {
		return templ, nil
	}

	var expandedTempl []domain.ExpandedTemplate

	for _, te := range templ {
		var newTemplate domain.ExpandedTemplate
		newTemplate.TemplateId = te.TemplateId
		newTemplate.Name = te.Name
		for _, et := range te.Exercises {
			ex, err := s.RepoExer.GetOneExercise(ctx, et.ExerciseId)
			if err != nil {
				return nil, err
			}
			newTemplate.Exercises = append(newTemplate.Exercises, domain.ExpandedExerciseTemplate{
				Exercise: ex,
				Set: et.Sets,
			})
		}

		expandedTempl = append(expandedTempl, newTemplate)
	}

	return expandedTempl, nil
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
			Set: et.Sets,
		})
	}

	return expandedTempl,nil
}

func (s *TemplateService) PostOneTemplate(ctx context.Context, payload *domain.Template) (string, error) {
	result, err := s.RepoTempl.InsertOneTemplate(ctx, *payload)
	if err != nil {
		return "", err
	}
	return result, nil
}

func (s *TemplateService) DeleteTemplate(ctx context.Context, id string) (string, error) {
	result, err := s.RepoTempl.DeleteOneTemplate(ctx, id)
	if err != nil {
		return "", err
	}
	return result, err
}
