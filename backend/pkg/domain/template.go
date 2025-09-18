package domain

import "context"

type Template struct {
	TemplateId  string   `bson:"templateId" json:"templateId"`
	Name        string   `bson:"name" json:"name"`
	ExerciseIds []string `bson:"exerciseIds" json:"exerciseIds"`
}

func (t *Template) Valid(ctx context.Context) map[string]string {
	problems := map[string]string{}

	if t.Name == "" {
		problems["name"] = "name is required"
	}
	
	if len(t.ExerciseIds) == 0 {
		problems["exerciseId"] = "exerciseId is required"
	}

	return problems
}
