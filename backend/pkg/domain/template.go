package domain

import (
	"context"
	"fmt"
)

type Template struct {
	TemplateId string               `json:"templateId"`
	Name       string               `json:"name"`
	Exercises  []ExerciseIdTemplate `json:"exercises"`
}

type TemplateRepository interface {
	FindAll(ctx context.Context, userId string) ([]Template, error)
	FindOne(ctx context.Context, id string, userId string) (Template, error)
	Insert(ctx context.Context, userId string, data Template) (string, error)
	Update(ctx context.Context, id string, userId string, data interface{}) (string, error)
	Delete(ctx context.Context, id string, userId string) (string, error)
}

type TemplateService interface {
	GetAll(ctx context.Context, userId string, include bool) (interface{}, error)
	GetOne(ctx context.Context, id string, userId string, include bool) (interface{}, error)
	Create(ctx context.Context, userId string, payload *Template) (string, error)
	Update(ctx context.Context, id string, userId string, payload interface{}) (string, error)
	Delete(ctx context.Context, id string, userId string) (string, error)
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
