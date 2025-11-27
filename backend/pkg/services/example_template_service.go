package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type ExampleTemplateServiceImpl struct {
	RepoTempl domain.ExampleTemplateRepository
	RepoExer  domain.ExerciseRepository
}

func (s *ExampleTemplateServiceImpl) GetAll(ctx context.Context) ([]domain.ExampleTemplate, error) {
	exampleTempl, err := s.RepoTempl.FindAll(ctx)
	if err != nil {
		return nil, err
	}
	return exampleTempl, nil	
}
