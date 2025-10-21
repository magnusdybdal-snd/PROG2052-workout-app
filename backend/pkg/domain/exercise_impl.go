package domain

import "context"

type ExercisesRepository interface {
	GetAllExercises(ctx context.Context, limit int) ([]Exercises, error)
	GetOneExercise(ctx context.Context, id string) (Exercises, error)
}

