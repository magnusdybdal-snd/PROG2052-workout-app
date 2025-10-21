package domain

import (
	"context"
	"fmt"
)


type Template struct {
	TemplateId string               `bson:"templateId" json:"templateId"`
	Name       string               `bson:"name" json:"name"`
	Exercises  []ExerciseIdTemplate `bson:"exercises" json:"exercises"`
}

type TemplateRepository interface {
	FindAll(ctx context.Context) ([]Template, error)
	FindOne(ctx context.Context, id string) (Template, error)
	Insert(ctx context.Context, data Template) (string, error)
	Update(ctx context.Context, id string, data interface{}) (string, error)
	Delete(ctx context.Context, id string) (string, error)
}

type TemplateService interface {
	GetAll(ctx context.Context, include bool) (interface{}, error)
	GetOne(ctx context.Context,id string, include bool) (interface{}, error)
	Create(ctx context.Context, payload *Template) (string, error)
	Update(ctx context.Context, id string, payload interface{}) (string, error)
	Delete(ctx context.Context, id string) (string, error)
}

func (t *Template) Valid(ctx context.Context) map[string]string {
	problems := map[string]string{}

	if t.Name == "" {
		problems["name"] = "name is required"
	}
	if len(t.Exercises) == 0 {
		problems["exercises"] = "at least one exercise is required"
	}
	for i, e := range t.Exercises {
		if e.ExerciseId == "" {
			problems[fmt.Sprintf("exercise[%d].exerciseId", i)] = "exercises id is required"
		}
	}
	if len(problems) == 0 {
		return nil
	}

	return problems
}
