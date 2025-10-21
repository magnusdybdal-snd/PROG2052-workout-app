package repository

import (
	"context"
	"fmt"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

type ExerciseRepository struct {
	Coll *mongo.Collection
}

func (r *ExerciseRepository) FindAll(ctx context.Context, limit int) ([]domain.Exercises, error) {
	var data []domain.Exercises
	filterOpts := options.Find().SetLimit(int64(limit))

	cursor, err := r.Coll.Find(ctx, bson.M{}, filterOpts)
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

func (r *ExerciseRepository) FindOne(ctx context.Context, id string) (domain.Exercises, error) {
	var data domain.Exercises
	filter := bson.M{"exerciseId": id}

	err := r.Coll.FindOne(ctx, filter).Decode(&data)
	if err != nil {
		var empty domain.Exercises
		return empty, err
	}

	return data, nil
}
