package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)



type ExerciseServiceImpl struct {
	Repo *repository.ExerciseRepository
}

func (s *ExerciseServiceImpl) GetAll(ctx context.Context, limit int) ([]domain.Exercises, error) {
	return s.Repo.FindAll(ctx, limit)
}

func (s *ExerciseServiceImpl) GetOne(ctx context.Context, id string) (domain.Exercises,error) {
	return s.Repo.FindOne(ctx,id)
}
