package domain

import "context"

type Exercises struct {
	ExerciseId       string   `json:"exerciseId"`
	Name             string   `json:"name"`
	TargetMuscles    []string `json:"targetMuscles"`
	BodyParts        []string `json:"bodyParts"`
	Equipments       []string `json:"equipments"`
	SecondaryMuscles []string `json:"secondaryMuscles"`
	GifUrl           string   `json:"gifUrl"`
	Instructions     []string `json:"instructions"`
}

// Domain interface for db repository
type ExerciseRepository interface {
	FindAll(ctx context.Context, limit int) ([]Exercises, error)
	FindOne(ctx context.Context, id string) (Exercises, error)
}

// Domain interface for service implementation
type ExerciseService interface {
	GetAll(ctx context.Context, limit int, url string) ([]Exercises, error)
	GetOne(ctx context.Context, id string, url string) (Exercises, error)
}

type TypeSet int32

const (
	Drop TypeSet = iota
	Failure
	Warmup
)

type Set struct {
	Rep  int32   `json:"rep"`
	Kg   float64 `json:"kg"`
	Type TypeSet `json:"typeSet"`
}

// Exercises with id and name
type ExerciseIdTemplate struct {
	ExerciseId string `json:"exerciseId"`
	Name       string `json:"name"`
	Sets       []Set  `json:"sets"`
}

// Exercise with everything
type ExpandedExerciseTemplate struct {
	Exercise Exercises `json:"exercise"`
	Set      []Set     `json:"sets"`
}
