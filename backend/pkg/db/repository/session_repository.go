package repository

import (
	"context"
	"fmt"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type SessionRepository struct {
	Coll *mongo.Collection
}

func (r *SessionRepository) GetAllSession(ctx context.Context) ([]domain.Session, error) {
	var data []domain.Session
	cursor, err := r.Coll.Find(ctx, bson.M{})
	if err != nil {
		return nil, err
	}
	if err := cursor.All(ctx, &data); err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)
	if len(data) == 0 {
		return nil, fmt.Errorf("no data found")
	}

	return data, nil
}
