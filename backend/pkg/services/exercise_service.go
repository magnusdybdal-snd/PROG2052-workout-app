package services

import (
	"context"
	"sync"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

type ExerciseServiceImpl struct {
	Repo domain.ExerciseRepository

	// Caching members
	cache         []domain.Exercises
	cacheAt       time.Time
	cacheDuration time.Duration

	// lock for race conditions
	mu sync.RWMutex

}

func NewExerciseService(exRepo domain.ExerciseRepository) *ExerciseServiceImpl {
	return &ExerciseServiceImpl{
		Repo: exRepo,
		cacheDuration: 30 * time.Second,
	}
}

func (s *ExerciseServiceImpl) GetAll(ctx context.Context, limit int, url string) ([]domain.Exercises, error) {
	//check cache

	s.mu.RLock() // mutex lock
	if s.cache != nil && time.Since(s.cacheAt) < s.cacheDuration {
		cached := make([]domain.Exercises, len(s.cache))
		copy(cached,s.cache)
		s.mu.RUnlock()
		return cached, nil
	}
	s.mu.RUnlock() // unlock mutex

	// expired cache, regular db operations
	exercises, err := s.Repo.FindAll(ctx, limit)
	if err != nil {
		return nil, err
	}

	// add gif url
	for i := range exercises {
		exercises[i].GifUrl = url + exercises[i].ExerciseId + ".gif"
	}
	// copy over for caching
	s.mu.Lock()
	s.cache = exercises
	s.cacheAt = time.Now()
	s.mu.Unlock()

	return exercises, nil
}

func (s *ExerciseServiceImpl) GetOne(ctx context.Context, id string, url string) (domain.Exercises, error) {
	exercise, err := s.Repo.FindOne(ctx, id)
	if err != nil {
		return domain.Exercises{}, err
	}
	exercise.GifUrl = url + exercise.ExerciseId + ".gif"

	return exercise, nil
}
