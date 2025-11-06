package domain

import (
	"context"
	"fmt"
	"time"
)

type Session struct {
	SessionId string               `json:"sessionId"`
	Name      string               `json:"name"` // get this from template
	Exercises []ExerciseIdTemplate `json:"exercises"`
	Duration  string               `json:"duration"`
	Date      string               `json:"date"`
	Note      string               `json:"note"`
}

// Repository implementation
type SessionRepository interface {
	FindAll(ctx context.Context, userId string) ([]Session, error)
	Insert(ctx context.Context, userId string,data Session) (string, error)
	Update(ctx context.Context, id string, userId string,data Session) (string, error)
	Delete(ctx context.Context, id string, userId string) (string, error)
}

// Service implementation
type SessionService interface {
	GetAll(ctx context.Context, userId string, include bool) (interface{}, error)
	Create(ctx context.Context, payload *Session) (string, error)
	Update(ctx context.Context, id string, payload interface{}) (string, error)
	Delete(ctx context.Context, id string) (string, error)
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
		_, err := time.Parse("15:04:05", s.Duration)
		if err != nil {
			problems["duration"] = "duration must be in format hh:mm:ss"
		}
	}
	// iso 9601
	if s.Date == "" {
		problems["date"] = "date is required"
	} else {
		_, err := time.Parse("2006-01-02", s.Date)
		if err != nil {
			problems["date"] = "date must be in format yyyy-mm-dd"
		}
	}

	return problems
}

type ExpandedSession struct {
	SessionId string                     `json:"sessionId"`
	Name      string                     `json:"name"` // get this from template
	Exercises []ExpandedExerciseTemplate `json:"exercises"`
	Duration  string                     `json:"duration"`
	Date      string                     `json:"date"`
	Note      string                     `json:"note"`
}
