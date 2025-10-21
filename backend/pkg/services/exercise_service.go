package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)



type ExerciseServiceImpl struct {
	Repo *repository.ExerciseRepository
}

func (s *ExerciseServiceImpl) GetAllExercises(ctx context.Context, limit int) ([]domain.Exercises, error) {
	return s.Repo.GetAllExercises(ctx, limit)
}

func (s *ExerciseServiceImpl) GetOneExercise(ctx context.Context, id string) (domain.Exercises,error) {
	return s.Repo.GetOneExercise(ctx,id)
}
