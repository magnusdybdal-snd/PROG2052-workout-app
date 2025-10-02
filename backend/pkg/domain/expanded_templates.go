package domain

type ExpandedTemplate struct {
	TemplateId string                     `bson:"templateId" json:"templateId"`
	Name       string                     `bson:"name" json:"name"`
	Exercises  []ExpandedExerciseTemplate `bson:"exercises" json:"exercises"`
}
