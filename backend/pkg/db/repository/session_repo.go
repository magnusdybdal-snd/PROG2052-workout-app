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

func (r *SessionRepository) InsertSession(ctx context.Context, data domain.Session) (string, error) {
	result, err := r.Coll.InsertOne(ctx, data)
	if err != nil {
		return "", err
	}
	id := fmt.Sprintf("%s",result.InsertedID)
	return id, nil
}

func (r *SessionRepository) UpdateOneSession(ctx context.Context, id string, data interface{}) (string, error) {
	filter := bson.M{"sessionId": id}
	result, err := r.Coll.ReplaceOne(ctx,filter, data)
	if err != nil {
		return "", err
	}
	if result.MatchedCount == 0 {
		return "", fmt.Errorf("no session found with this id: %s",id)
	}
	return id, nil
}

func (r *SessionRepository) DeleteSession(ctx context.Context, id string) (string, error) {
	filter := bson.M{"sessionId": id}
	result, err := r.Coll.DeleteOne(ctx,filter)
	if err != nil {
		return "", err
	}
	if result.DeletedCount == 0 {
		return "", fmt.Errorf("no session with id %s", id)
	}

	return id, nil
}
