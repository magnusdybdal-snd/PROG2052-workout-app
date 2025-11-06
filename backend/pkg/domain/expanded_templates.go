package domain

type ExpandedTemplate struct {
	TemplateId string                     `json:"templateId"`
	UserId     string                     `json:"userId"`
	Name       string                     `json:"name"`
	Exercises  []ExpandedExerciseTemplate `json:"exercises"`
}
