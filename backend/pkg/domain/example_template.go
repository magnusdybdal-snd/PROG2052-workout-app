package domain

import "context"

type ExampleTemplate struct {
	TemplateId string               `json:"templateId"`
	Name       string               `json:"name"`
	Exercises  []ExerciseIdTemplate `json:"exercises"`
}

type ExampleTemplateRepository interface {
	FindAll(ctx context.Context) ([]ExampleTemplate, error)
}

type ExampleTemplateService interface {
	GetAll(ctx context.Context) ([]ExampleTemplate, error)
}
