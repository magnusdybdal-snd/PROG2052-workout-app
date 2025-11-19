package repository

import (
	"context"
	"fmt"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/models"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type SessionRepository struct {
	Coll *mongo.Collection
}

func (r *SessionRepository) FindAll(ctx context.Context, userId string) ([]domain.Session, error) {
	var entity []models.SessionEntity
	filter := bson.M{"userId": userId}
	cursor, err := r.Coll.Find(ctx, filter)
	if err != nil {
		return nil, err
	}

	defer cursor.Close(ctx)

	if err := cursor.All(ctx, &entity); err != nil {
		return nil, err
	}
	entityLen := len(entity)

	if entityLen == 0 {
		return []domain.Session{},nil 
	}
	response := make([]domain.Session, entityLen)
	for i, v := range entity {
		response[i] = toDomainSession(v)
	}

	return response, nil
}

func (r *SessionRepository) Insert(ctx context.Context, userId string, data domain.Session) (string, error) {
	entity := toEntitySession(data, userId)
	result, err := r.Coll.InsertOne(ctx, entity)
	if err != nil {
		return "", err
	}
	id := fmt.Sprintf("%s", result.InsertedID)
	return id, nil
}

func (r *SessionRepository) Update(ctx context.Context, id string, userId string, data domain.Session) (string, error) {
	entity := toEntitySession(data, userId)
	filter := bson.M{"sessionId": id, "userId": userId}
	result, err := r.Coll.ReplaceOne(ctx, filter, entity)
	if err != nil {
		return "", err
	}
	if result.MatchedCount == 0 {
		return "", fmt.Errorf("no session found with this id: %s", id)
	}
	return id, nil
}

func (r *SessionRepository) Delete(ctx context.Context, id string, userId string) (string, error) {
	filter := bson.M{"sessionId": id, "userId":userId}
	result, err := r.Coll.DeleteOne(ctx, filter)
	if err != nil {
		return "", err
	}
	if result.DeletedCount == 0 {
		return "", fmt.Errorf("no session with id %s", id)
	}

	return id, nil
}
