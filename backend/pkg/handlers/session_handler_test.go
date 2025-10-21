package handlers_test

import (
	"context"
	"net/http"
	"net/http/httptest"
	"testing"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
)

// mockSessionService implements domain.SessionService
type mockSessionService struct {
	GetAllFn  func(ctx context.Context, include bool) (interface{}, error)
	PostFn    func(ctx context.Context, payload *domain.Session) (string, error)
	PutFn     func(ctx context.Context, id string, payload interface{}) (string, error)
	DeleteFn  func(ctx context.Context, id string) (string, error)
}

func (m *mockSessionService) GetAllSession(ctx context.Context, include bool) (interface{}, error) {
	return m.GetAllFn(ctx, include)
}
func (m *mockSessionService) PostSession(ctx context.Context, payload *domain.Session) (string, error) {
	return m.PostFn(ctx, payload)
}
func (m *mockSessionService) PutSession(ctx context.Context, id string, payload interface{}) (string, error) {
	return m.PutFn(ctx, id, payload)
}
func (m *mockSessionService) DeleteSession(ctx context.Context, id string) (string, error) {
	return m.DeleteFn(ctx, id)
}

func TestHandleSessionGet(t *testing.T) {
	mock := &mockSessionService{
		GetAllFn: func(ctx context.Context, include bool) (interface{}, error) {
			return []domain.Session{
				{SessionId: "1", Name: "Push Day", Duration: "PT1H"},
			}, nil
		},
	}
	req := httptest.NewRequest(http.MethodGet, "/sessions", nil)
	w := httptest.NewRecorder()

	handler := handlers.HandleSession(mock)
	handler(w, req)
}
