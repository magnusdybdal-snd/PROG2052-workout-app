package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)



type ExerciseServiceImpl struct {
	Repo *repository.ExerciseRepository
}

func (s *ExerciseServiceImpl) GetAll(ctx context.Context, limit int, url string) ([]domain.Exercises, error) {
	exercises, err := s.Repo.FindAll(ctx, limit)
	if err != nil {
		return nil, err
	}

	for i := range exercises {
		exercises[i].GifUrl = url + exercises[i].ExerciseId + ".gif"
	}
	return exercises,nil
}

func (s *ExerciseServiceImpl) GetOne(ctx context.Context, id string, url string) (domain.Exercises,error) {
	exercise,err := s.Repo.FindOne(ctx,id)
	if err != nil {
		return domain.Exercises{}, err
	}
	exercise.GifUrl = url + exercise.ExerciseId + ".gif"

	return exercise, nil
}
