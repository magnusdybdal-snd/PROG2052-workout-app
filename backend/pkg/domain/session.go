package domain

import (
	"context"
	"fmt"
	"time"
)

type Session struct {
	SessionId string               `bson:"sessionId" json:"sessionId"`
	Name      string               `bson:"name" json:"name"` // get this from template
	Exercises []ExerciseIdTemplate `bson:"exercises" json:"exercises"`
	Duration  string               `bson:"duration" json:"duration"`
	Date      string               `bson:"date" json:"date"`
	Note      string               `bson:"note" json:"note"`
}

func (s *Session) Valid(ctx context.Context) map[string]string {
	problems := map[string]string{}
	
	if s.SessionId == "" {
		problems["sessionId"] = "sessionId is required"
	}
	if s.Name == "" {
		problems["name"] = "name is required"
	}
	if len(s.Exercises) == 0 {
		problems["exercises"] = "at least one exercise is required"
	}
	for i, e := range s.Exercises {
		if e.ExerciseId == "" {
			problems[fmt.Sprintf("exercise[%d].exerciseId", i)] = "exercises id is required"
		}
	}
	// pt1h15m30s
	if s.Duration == "" {
		problems["duration"] = "duration is required"
	} else {
		_, err := time.Parse("15:04:05",s.Duration)
		if err != nil {
			problems["duration"] = "duration must be in format hh:mm:ss"
		}
	}
	// iso 9601
	if s.Date == "" {
		problems["date"] = "date is required"
	} else {
		_, err := time.Parse("2006-01-02",s.Date)
		if err != nil {
			problems["date"] = "date must be in format yyyy-mm-dd"
		}
	}

	return problems
}

type ExpandedSession struct {
	SessionId string                     `bson:"sessionId" json:"sessionId"`
	Name      string                     `bson:"name" json:"name"` // get this from template
	Exercises []ExpandedExerciseTemplate `bson:"exercises" json:"exercises"`
	Duration  string                     `bson:"duration" json:"duration"`
	Date      string                     `bson:"date" json:"date"`
	Note      string                     `bson:"note" json:"note"`
}
