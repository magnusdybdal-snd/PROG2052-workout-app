package domain

type ExpandedExerciseTemplate struct {
	Exercise Exercises `bson:"exercise" json:"exercise"`
	Set      []Set     `bson:"set" json:"set"`
}

type ExpandedTemplate struct {
	TemplateId string                     `bson:"templateId" json:"templateId"`
	Name       string                     `bson:"name" json:"name"`
	Exercises  []ExpandedExerciseTemplate `bson:"exercises" json:"exercises"`
}
