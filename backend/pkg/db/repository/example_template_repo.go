package repository

import (
	"context"
	"fmt"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

// Special collection for example templates
// uses the same domain as a regular template

type ExampleTemplateRepository struct {
	Coll *mongo.Collection
}

func (r *ExampleTemplateRepository) FindAll(ctx context.Context) ([]domain.ExampleTemplate,error){
	var data []domain.ExampleTemplate

	cursor, err := r.Coll.Find(ctx,bson.M{})
	if err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)

	if err := cursor.All(ctx,&data); err != nil {
		return nil, err
	}
	if len(data) == 0 {
		return nil, fmt.Errorf("no data found")
	}

	return data,nil
}
