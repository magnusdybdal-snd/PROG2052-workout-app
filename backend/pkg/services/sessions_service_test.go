package services_test

import (
	"context"
	"errors"
	"testing"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
)

// Mock database implementation
type mockRepo struct {
	GetAllFn func(ctx context.Context) ([]domain.Session, error)
	InsertFn func(ctx context.Context, data domain.Session) (string, error)
	UpdateFn func(ctx context.Context, id string, data interface{}) (string, error)
	DeleteFn func(ctx context.Context, id string) (string, error)
}

func (m *mockRepo) GetAllSession(ctx context.Context) ([]domain.Session, error) {
	return m.GetAllFn(ctx)
}
func (m *mockRepo) InsertSession(ctx context.Context, data domain.Session) (string, error) {
	return m.InsertFn(ctx, data)
}
func (m *mockRepo) UpdateOneSession(ctx context.Context, id string, data interface{}) (string, error) {
	return m.UpdateFn(ctx, id, data)
}
func (m *mockRepo) DeleteSession(ctx context.Context, id string) (string, error) {
	return m.DeleteFn(ctx, id)
}

// ---------- TESTS ----------

func TestGetAllSession(t *testing.T) {
	mock := &mockRepo{
		GetAllFn: func(ctx context.Context) ([]domain.Session, error) {
			return []domain.Session{{SessionId: "1", Name: "Leg Day"}}, nil
		},
	}
	svc := &services.SessionServiceImpl{Repo: mock}

	result, err := svc.GetAllSession(context.Background(), false)
	if err != nil {
		t.Fatal(err)
	}
	sessions := result.([]domain.Session)
	if len(sessions) != 1 || sessions[0].Name != "Leg Day" {
		t.Fatalf("unexpected session result: %+v", sessions)
	}
}

func TestPostSession_Error(t *testing.T) {
	mock := &mockRepo{
		InsertFn: func(ctx context.Context, data domain.Session) (string, error) {
			return "", errors.New("insert failed")
		},
	}
	svc := &services.SessionServiceImpl{Repo: mock}

	_, err := svc.PostSession(context.Background(), &domain.Session{Name: "Bad"})
	if err == nil {
		t.Fatalf("expected error, got nil")
	}
}

