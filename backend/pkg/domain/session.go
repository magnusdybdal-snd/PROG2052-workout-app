package domain

type Session struct {
	SessionId string               `bson:"sessionId" json:"sessionId"`
	Name      string               `bson:"name" json:"name"` // get this from template
	Exercises []ExerciseIdTemplate `bson:"exercises" json:"exercises"`
	Duration  string               `bson:"duration" json:"duration"`
	Date      string               `bson:"date" json:"date"`
	Note      string               `bson:"note" json:"note"`
}

type ExpandedSession struct {
	SessionId string                     `bson:"sessionId" json:"sessionId"`
	Name      string                     `bson:"name" json:"name"` // get this from template
	Exercises []ExpandedExerciseTemplate `bson:"exercises" json:"exercises"`
	Duration  string                     `bson:"duration" json:"duration"`
	Date      string                     `bson:"date" json:"date"`
	Note      string                     `bson:"note" json:"note"`
}
