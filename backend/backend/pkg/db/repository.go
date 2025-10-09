package db

import "go.mongodb.org/mongo-driver/v2/mongo"

type Repositoty[T any] struct {
	Coll *mongo.Collection
}
