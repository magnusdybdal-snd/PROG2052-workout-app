package domain

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

type TypeSet int

const (
	Drop TypeSet = iota
	Failure
	Warmup
)

type Set struct {
	Rep  int32   `bson:"rep" json:"rep"`
	Kg   int32   `bson:"kg" json:"kg"`
	Type TypeSet `bson:"TypeSet" json:"TypeSet"`
}

type ExerciseIdTemplate struct {
	ExerciseId string `bson:"exerciseId" json:"exerciseId"` // Changed in service layer to exericise
	Set        []Set  `bson:"set" json:"set"`
}

type ExpandedExerciseTemplate struct {
	Exercise Exercises `bson:"exercise" json:"exercise"`
	Set      []Set     `bson:"set" json:"set"`
}

// TODO:
// Set up interfaces here for exercises
// This way, services only implements this interface
// will be easier to test afterwards
