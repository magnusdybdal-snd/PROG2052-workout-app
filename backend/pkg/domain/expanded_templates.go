package domain

type ExpandedTemplate struct {
	TemplateId string                     `bson:"templateId" json:"templateId"`
	UserId     string                     `bson:"userId" json:"userId"`
	Name       string                     `bson:"name" json:"name"`
	Exercises  []ExpandedExerciseTemplate `bson:"exercises" json:"exercises"`
}
