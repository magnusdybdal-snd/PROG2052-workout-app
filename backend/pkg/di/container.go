package di

import (
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.uber.org/zap"
)

/*
	Dependency injection
	Initilize services and repositories
*/

const (
	dbName = "TrainingApp"
	exerciseColl = "exercises"
	templateColl = "templates"
	exampleTemplateColl = "exampleTemplate"
	sessionColl  = "sessions"
)

type ServiceContainer struct {
	ExerciseService domain.ExerciseService
	TemplateService domain.TemplateService
	ExampleTemplateService domain.ExampleTemplateService
	SessionService  domain.SessionService
	DB              *mongo.Client
	Logger          *zap.Logger
}

// Starting up all services and repositories
func NewContainer(cfg *config.Config) (*ServiceContainer, error) {
	mongoDB, err := db.InitDB(cfg.UriDB)
	if err != nil {
		return nil, err
	}
	loggerService, err := utils.NewLogger(cfg.Mode)
	if err != nil {
		return nil, err
	}

	// Starting repository for data access
	exerciseRepo := &repository.ExerciseRepository{
		Coll: mongoDB.Database(dbName).Collection(exerciseColl),
	}
	templateRepo := &repository.TemplateRepository{
		Coll: mongoDB.Database(dbName).Collection(templateColl),
	}
	
	exampleTemplateRepo := &repository.ExampleTemplateRepository{
		Coll: mongoDB.Database(dbName).Collection(exampleTemplateColl),
	}

	sessionRepo := &repository.SessionRepository{
		Coll: mongoDB.Database(dbName).Collection(sessionColl),
	}

	// Starting up Services
	exerciseService := services.NewExerciseService(exerciseRepo)

	templateService := services.NewTemplateService(templateRepo, exerciseRepo)

	exampleTemplateService := &services.ExampleTemplateServiceImpl{
		RepoTempl: exampleTemplateRepo,
		RepoExer: exerciseRepo,
	}

	sessionService := &services.SessionServiceImpl{
		Repo:     sessionRepo,
		RepoExer: exerciseRepo,
	}

	
	return &ServiceContainer{
		ExerciseService: exerciseService,
		TemplateService: templateService,
		ExampleTemplateService: exampleTemplateService,
		SessionService:  sessionService,
		DB:              mongoDB,
		Logger:          loggerService,
	}, nil
}
