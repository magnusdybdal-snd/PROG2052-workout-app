package repository

import (
	"context"
	"fmt"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/models"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type TemplateRepository struct {
	Coll *mongo.Collection
}

func (r *TemplateRepository) FindAll(ctx context.Context, userId string) ([]domain.Template, error) {
	var entity []models.TemplateEntity

	filter := bson.M{"userId": userId}

	cursor, err := r.Coll.Find(ctx, filter)
	if err != nil {
		return nil, err
	}
	if err := cursor.All(ctx, &entity); err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)
	entityLen := len(entity)
	if entityLen == 0 {
		return nil, fmt.Errorf("no data found for user")
	}

	// convert to domain
	response := make([]domain.Template, entityLen)
	for i, v := range entity {
		response[i] = toDomainTemplate(v) // converts entity to domain model
	}

	return response, nil
}

func (r *TemplateRepository) FindOne(ctx context.Context, id string, userId string) (domain.Template, error) {
	var entity models.TemplateEntity
	filter := bson.M{"templateId": id, "userId": userId}

	err := r.Coll.FindOne(ctx, filter).Decode(&entity)
	if err != nil {
		var empty domain.Template
		return empty, err
	}
	response := toDomainTemplate(entity)

	return response, nil
}

func (r *TemplateRepository) Insert(ctx context.Context, userId string, data domain.Template) (string, error) {
	entity := toEntityTemplate(data, userId)
	result, err := r.Coll.InsertOne(ctx, entity)
	if err != nil {
		return "", err
	}
	id := fmt.Sprintf("%s", result.InsertedID)
	return id, nil
}

func (r *TemplateRepository) Update(ctx context.Context, id string, userId string, data domain.Template) (string, error) {
	entity := toEntityTemplate(data, userId)
	filter := bson.M{"templateId": id, "userId": userId}
	result, err := r.Coll.ReplaceOne(ctx, filter, entity)
	if err != nil {
		return "", err
	}
	if result.MatchedCount == 0 {
		return "", fmt.Errorf("no template found with this id: %s", id)
	}
	return id, nil
}

func (r *TemplateRepository) Delete(ctx context.Context, id string, userId string) (string, error) {
	filter := bson.M{"templateId": id, "userId":userId}
	result, err := r.Coll.DeleteOne(ctx, filter)
	if err != nil {
		return "", err
	}
	if result.DeletedCount == 0 {
		return "", fmt.Errorf("no session with id %s", id)
	}
	return id, nil
}
