package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)


type SessionService struct {
	Repo *repository.SessionRepository
	RepoExer *repository.ExerciseRepository
}

func (s *SessionService) GetAllSession(ctx context.Context, include bool) (interface{}, error) {
	sess, err := s.Repo.GetAllSession(ctx)
	if err != nil {
		return nil, err
	}
	if !include {
		return sess,nil
	}
	
	var expandedSession []domain.ExpandedSession
	for _, se := range sess {
		var newSession domain.ExpandedSession
		newSession.SessionId = se.SessionId
		newSession.Name = se.Name
		newSession.Duration = se.Duration 
		newSession.Date = se.Date
		newSession.Note = se.Note
		for _, et := range se.Exercises {
			ex, err := s.RepoExer.GetOneExercise(ctx,et.ExerciseId)
			if err != nil {
				return nil, err
			}
			newSession.Exercises = append(newSession.Exercises, domain.ExpandedExerciseTemplate{
				Exercise: ex,
				Set: et.Sets,
			})
		}
		expandedSession = append(expandedSession, newSession)
		
	}

	return expandedSession, nil
}

func (s *SessionService) PostSession(ctx context.Context, payload *domain.Session) (string, error) {
	result, err := s.Repo.InsertSession(ctx,*payload)
	if err != nil {
		return "",err 
	}
	return result, nil
}
