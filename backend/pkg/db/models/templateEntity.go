package models

type TemplateEntity struct {
	TemplateId string                   `bson:"templateId"`
	UserId     string                   `bson:"userId"`
	Name       string                   `bson:"name"`
	Exercises  []ExerciseTemplateEntity `bson:"exercises"`
}

type TypeSet int32

const (
	Drop TypeSet = iota
	Failure
	Warmup
)

type Set struct {
	Rep  int32   `bson:"rep"`
	Kg   float64 `bson:"kg"`
	Type TypeSet `bson:"typeSet"`
}

// Exercises with id and name
type ExerciseTemplateEntity struct {
	ExerciseId string `bson:"exerciseId"`
	Name       string `bson:"name"`
	Sets       []Set  `bson:"sets"`
}
