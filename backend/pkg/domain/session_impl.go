package domain

import "context"

// Repository implementation
type SessionRepository interface {
	GetAllSession(ctx context.Context) ([]Session, error)
	InsertSession(ctx context.Context, data Session) (string, error)
	UpdateOneSession(ctx context.Context, id string, data interface{}) (string, error)
	DeleteSession(ctx context.Context, id string) (string, error)
}

// Service implementation
type SessionService interface {
	GetAllSession(ctx context.Context, include bool) (interface{}, error)
	PostSession(ctx context.Context, payload *Session) (string, error)
	PutSession(ctx context.Context, id string, payload interface{}) (string, error)
	DeleteSession(ctx context.Context, id string) (string, error)
}


