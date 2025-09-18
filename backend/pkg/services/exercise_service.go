package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
)



type ExerciseService struct {
	Repo *db.Repositoty[domain.Exercises]
}

func (s *ExerciseService) GetAllExercises(ctx context.Context) ([]domain.Exercises, error) {
	return s.Repo.GetAll(ctx)
}

func (s *ExerciseService) GetOneExercise(ctx context.Context, id string) (domain.Exercises,error) {
	return s.Repo.GetOne(ctx,bson.M{"exerciseId":id})
}
