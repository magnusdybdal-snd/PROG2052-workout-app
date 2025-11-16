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

type ServiceContainer struct {
	ExerciseService domain.ExerciseService
	TemplateService domain.TemplateService
	SessionService  domain.SessionService
	DB *mongo.Client
	Logger *zap.Logger
}

// Starting up all services and repositories
func NewContainer(cfg *config.Config) (*ServiceContainer, error) {
	mongoDB, err := db.InitDB(cfg.UriDB)
	if err != nil {
		return nil,err
	}
	loggerService, err := utils.NewLogger(cfg.Mode)
	if err != nil {
		return nil,err
	}

	// Starting repository for data access
	exerciseRepo := &repository.ExerciseRepository{
		Coll: mongoDB.Database("TrainingApp").Collection("exercises"),
	}
	templateRepo := &repository.TemplateRepository{
		Coll: mongoDB.Database("TrainingApp").Collection("templates"),
	}
	sessionRepo := &repository.SessionRepository{
		Coll: mongoDB.Database("TrainingApp").Collection("sessions"),
	}

	// Starting up Services
	exerciseService := services.NewExerciseService(exerciseRepo)

	templateService := services.NewTemplateService(templateRepo, exerciseRepo)

	sessionService := &services.SessionServiceImpl{
		Repo:     sessionRepo,
		RepoExer: exerciseRepo,
	}
	return &ServiceContainer{
		ExerciseService: exerciseService,
		TemplateService: templateService,
		SessionService: sessionService,
		DB: mongoDB,
		Logger: loggerService,
	},nil
}
