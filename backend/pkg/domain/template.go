package domain

import "context"

type TypeSet int

const (
	Drop TypeSet = iota
	Failure
	Warmup
)

type Set struct {
	Rep  int32   `bson:"rep" json:"rep"`
	Kg   int32   `bson:"kg" json:"kg"`
	Type TypeSet `bson:"TypeSet" json:"TypeSet"`
}

type ExerciseTemplate struct {
	ExerciseId string `bson:"exerciseId" json:"exerciseId"` // Changed in service layer to exericise
	Set        []Set  `bson:"set" json:"set"`
}

type Template struct {
	TemplateId string             `bson:"templateId" json:"templateId"`
	Name       string             `bson:"name" json:"name"`
	Exercises  []ExerciseTemplate `bson:"exercises" json:"exercises"`
}

func (t *Template) Valid(ctx context.Context) map[string]string {
	problems := map[string]string{}

	if t.Name == "" {
		problems["name"] = "name is required"
	}

	return problems
}
