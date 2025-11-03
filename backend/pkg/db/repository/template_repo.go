package repository

import (
	"context"
	"fmt"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type TemplateRepository struct {
	Coll *mongo.Collection
}

func (r *TemplateRepository) FindAll(ctx context.Context, userId int) ([]domain.Template, error) {
	var data []domain.Template

	filter := bson.M{"userId": userId}

	cursor, err := r.Coll.Find(ctx, filter)
	if err != nil {
		return nil, err
	}
	if err := cursor.All(ctx, &data); err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)
	if len(data) == 0 {
		return nil, fmt.Errorf("no data found for user")
	}

	return data, nil
}

func (r *TemplateRepository) FindOne(ctx context.Context, id string) (domain.Template, error) {
	var data domain.Template
	filter := bson.M{"templateId": id}

	err := r.Coll.FindOne(ctx, filter).Decode(&data)
	if err != nil {
		var empty domain.Template
		return empty, err
	}

	return data, nil
}

func (r *TemplateRepository) Insert(ctx context.Context, data domain.Template) (string, error) {
	result, err := r.Coll.InsertOne(ctx, data)
	if err != nil {
		return "", err
	}
	id := fmt.Sprintf("%s", result.InsertedID)
	return id, nil
}

func (r *TemplateRepository) Update(ctx context.Context, id string, data interface{}) (string, error) {
	filter := bson.M{"templateId": id}
	result, err := r.Coll.ReplaceOne(ctx, filter, data)
	if err != nil {
		return "", err
	}
	if result.MatchedCount == 0 {
		return "", fmt.Errorf("no template found with this id: %s", id)
	}
	return id, nil
}

func (r *TemplateRepository) Delete(ctx context.Context, id string) (string, error) {
	filter := bson.M{"templateId": id}
	result, err := r.Coll.DeleteOne(ctx, filter)
	if err != nil {
		return "", err
	}
	if result.DeletedCount == 0 {
		return "", fmt.Errorf("no session with id %s", id)
	}
	return id, nil
}
