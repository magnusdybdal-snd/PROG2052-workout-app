package di

import (
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/repository"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/services"
	"go.mongodb.org/mongo-driver/v2/mongo"
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
}

func NewContainer(cfg *config.Config) (*ServiceContainer, error) {
	mongoDB, err := db.InitDB(cfg.UriDB)
	if err != nil {
		return nil,err
	}
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
	exerciseService := &services.ExerciseServiceImpl{
		Repo: exerciseRepo,
	}

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
	},nil
}
