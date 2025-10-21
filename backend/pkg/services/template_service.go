package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type TemplateServiceImpl struct {
	RepoTempl domain.TemplateRepository
	RepoExer domain.ExerciseRepository
}

func NewTemplateService(
	rTempl domain.TemplateRepository, 
	rExer domain.ExerciseRepository,
) *TemplateServiceImpl {
	return &TemplateServiceImpl{
		RepoTempl: rTempl,
		RepoExer: rExer,
	}
}

func (s *TemplateServiceImpl) GetAll(ctx context.Context, include bool) (interface{}, error) {
	templ, err := s.RepoTempl.FindAll(ctx)
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
			ex, err := s.RepoExer.FindOne(ctx, et.ExerciseId)
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

func (s *TemplateServiceImpl) GetOne(ctx context.Context,id string, include bool) (interface{}, error) {
	templ,err := s.RepoTempl.FindOne(ctx,id)
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
		ex, err := s.RepoExer.FindOne(ctx,et.ExerciseId)
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

func (s *TemplateServiceImpl) Create(ctx context.Context, payload *domain.Template) (string, error) {
	result, err := s.RepoTempl.Insert(ctx, *payload)
	if err != nil {
		return "", err
	}
	return result, nil
}

func (s *TemplateServiceImpl) Update(ctx context.Context, id string, payload interface{}) (string, error) {
	result, err := s.RepoTempl.Update(ctx, id, payload)
	if err != nil {
		return "", err
	}
	return result, nil
}

func (s *TemplateServiceImpl) Delete(ctx context.Context, id string) (string, error) {
	result, err := s.RepoTempl.Delete(ctx, id)
	if err != nil {
		return "", err
	}
	return result, err
}
