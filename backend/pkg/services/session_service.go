package services

import (
	"context"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type SessionServiceImpl struct {
	Repo     domain.SessionRepository
	RepoExer domain.ExerciseRepository
}

func (s *SessionServiceImpl) GetAll(ctx context.Context, userId string, include bool) (interface{}, error) {
	sess, err := s.Repo.FindAll(ctx, userId)
	if err != nil {
		return domain.Session{}, err
	}
	if !include {
		return sess, nil
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
			ex, err := s.RepoExer.FindOne(ctx, et.ExerciseId)
			if err != nil {
				return domain.Session{}, err
			}
			newSession.Exercises = append(newSession.Exercises, domain.ExpandedExerciseTemplate{
				Exercise: ex,
				Set:      et.Sets,
			})
		}
		expandedSession = append(expandedSession, newSession)

	}

	return expandedSession, nil
}

func (s *SessionServiceImpl) Create(ctx context.Context, userId string, payload *domain.Session) (string, error) {
	result, err := s.Repo.Insert(ctx, userId, *payload)
	if err != nil {
		return "", err
	}
	return result, nil
}

func (s *SessionServiceImpl) Update(ctx context.Context, id string, userId string, payload domain.Session) (string, error) {
	result, err := s.Repo.Update(ctx, id, userId, payload)
	if err != nil {
		return "", err
	}
	return result, nil
}

func (s *SessionServiceImpl) Delete(ctx context.Context, id string, userId string) (string, error) {
	result, err := s.Repo.Delete(ctx, id, userId)
	if err != nil {
		return "", err
	}
	return result, nil
}
