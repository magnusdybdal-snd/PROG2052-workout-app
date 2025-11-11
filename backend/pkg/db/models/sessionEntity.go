package models

type SessionEntity struct {
	SessionId string                   `bson:"sessionId"`
	UserId    string                   `bson:"userId"`
	Name      string                   `bson:"name"` // get this from template
	Exercises []ExerciseTemplateEntity `bson:"exercises"`
	Duration  string                   `bson:"duration"`
	Date      string                   `bson:"date"`
	Note      string                   `bson:"note"`
}
