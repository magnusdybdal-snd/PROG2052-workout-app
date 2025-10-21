package domain

import "context"

type SessionRepository interface {
	GetAllSession(ctx context.Context) ([]Session, error)
	InsertSession(ctx context.Context, data Session) (string, error)
	UpdateOneSession(ctx context.Context, id string, data interface{}) (string, error)
	DeleteSession(ctx context.Context, id string) (string, error)
}
