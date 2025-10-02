package db

import (
	"context"
	"fmt"
	"log"

	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
	"go.mongodb.org/mongo-driver/v2/mongo/readpref"
)

func InitDB(uri string) (*mongo.Client, error) {
	log.Println("Initializing connection to database")
	client, err := mongo.Connect(options.Client().ApplyURI(uri))
	if err != nil {
		return nil, err
	}
	if err := client.Ping(context.TODO(), readpref.Primary()); err != nil {
		return nil,err
	}
	fmt.Println("Pinging the database")
	return client, nil
}

func CloseDB(db *mongo.Client) error {
	if err := db.Disconnect(context.TODO()); err != nil {
		return err
	}
	log.Println("Successfully closed mongo db connection")
	return nil
}
