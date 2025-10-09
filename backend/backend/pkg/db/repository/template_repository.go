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

func (r *TemplateRepository) GetAllTemplates(ctx context.Context) ([]domain.Template, error) {
	var data []domain.Template
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

func (r *TemplateRepository) GetOneTemplate(ctx context.Context, id string) (domain.Template, error) {
	var data domain.Template
	filter := bson.M{"templateId":id}

	err := r.Coll.FindOne(ctx,filter).Decode(&data)
	if err != nil {
		var empty domain.Template
		return empty, err
	}

	return data, nil
}

func (r *TemplateRepository) InsertOneTemplate(ctx context.Context, data domain.Template) (string, error) {
	result, err := r.Coll.InsertOne(ctx,data)
	if err != nil {
		return "", err
	}
	id := fmt.Sprintf("%s",result.InsertedID)
	return id, nil
}
