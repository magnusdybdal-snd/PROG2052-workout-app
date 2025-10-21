package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type SessionServiceImpl struct {
	Repo domain.SessionRepository
	RepoExer domain.ExerciseRepository
}

func (s *SessionServiceImpl) GetAll(ctx context.Context, include bool) (interface{}, error) {
	sess, err := s.Repo.FindAll(ctx)
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
			ex, err := s.RepoExer.FindOne(ctx,et.ExerciseId)
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

func (s *SessionServiceImpl) Create(ctx context.Context, payload *domain.Session) (string, error) {
	result, err := s.Repo.Insert(ctx,*payload)
	if err != nil {
		return "",err 
	}
	return result, nil
}

func (s *SessionServiceImpl) Update(ctx context.Context, id string, payload interface{}) (string, error) {
	result, err := s.Repo.Update(ctx, id, payload)
	if err != nil {
		return "", err
	}
	return result, nil
}

func (s *SessionServiceImpl) Delete(ctx context.Context, id string) (string, error) {
	result, err := s.Repo.Delete(ctx,id)
	if err != nil {
		return "", err
	}
	return result, nil
}
