package models

type ExercisesEntity struct {
	Id               string   `bson:"exerciseId"`
	Name             string   `bson:"name"`
	TargetMuscles    []string `bson:"targetMuscles"`
	BodyParts        []string `bson:"bodyParts"`
	Equipments       []string `bson:"equipments"`
	SecondaryMuscles []string `bson:"secondaryMuscles"`
	GifUrl           string   `bson:"gifUrl"`
	Instructions     []string `bson:"instructions"`
}
