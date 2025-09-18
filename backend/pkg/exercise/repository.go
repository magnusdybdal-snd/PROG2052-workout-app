package exercise

import (
	"context"
	"fmt"

	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

type Exercises struct {
	Id               string   `bson:"exerciseId" json:"exerciseId"`
	Name             string   `bson:"name" json:"name"`
	TargetMuscles    []string `bson:"targetMuscles" json:"targetMuscles"`
	BodyParts        []string `bson:"bodyParts" json:"bodyParts"`
	Equipments       []string `bson:"equipments" json:"equipments"`
	SecondaryMuscles []string `bson:"secondaryMuscles" json:"secondaryMuscles"`
	GifUrl           string   `bson:"gifUrl" json:"gifUrl"`
	Instructions     []string `bson:"instructions" json:"instructions"`
}

var (
	emptyAll []Exercises
	emptyOne Exercises
)

type ExercisesRepository struct {
	Coll *mongo.Collection
}

func (r *ExercisesRepository) GetAll(ctx context.Context) ([]Exercises, error) {
	var exercises []Exercises
	cursor, err := r.Coll.Find(ctx, bson.M{})
	if err != nil {
		return emptyAll, err
	}

	if err := cursor.All(ctx, &exercises); err != nil {
		return emptyAll, err
	}

	defer cursor.Close(ctx)

	if len(exercises) == 0 {
		return emptyAll, fmt.Errorf("no exercises found")
	}

	return exercises, nil
}

func (r *ExercisesRepository) GetOne(ctx context.Context, id string) (Exercises, error) {
	var exercise Exercises

	filter := bson.M{"exerciseId": id}

	err := r.Coll.FindOne(ctx,filter).Decode(&exercise)
	if err != nil {
		return emptyOne, err
	}

	return exercise, nil
}
