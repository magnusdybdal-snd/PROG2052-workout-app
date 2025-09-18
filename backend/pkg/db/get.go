package db

import (
	"context"
	"fmt"

	"go.mongodb.org/mongo-driver/v2/bson"
)

func (r *Repositoty[T]) GetAll(ctx context.Context) ([]T, error) {
	var data []T
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

func (r *Repositoty[T]) GetOne(ctx context.Context, filter bson.M) (T, error) {
	var data T

	err := r.Coll.FindOne(ctx,filter).Decode(&data)
	if err != nil {
		var empty T
		return empty, err
	}

	return data, nil
}
