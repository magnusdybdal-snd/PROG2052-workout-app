package domain

type Template struct {
	TemplateId  string   `bson:"templateId" json:"templateId"`
	Name        string   `bson:"name" json:"name"`
	ExerciseIds []string `bson:"exerciseIds" json:"exerciseIds"`
}
