package repository

import (
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/db/models"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
)

// TEMPLATES
/*
	Converts Entity -> domain
	removes the userId
*/
func toDomainTemplate(e models.TemplateEntity) domain.Template {
	return domain.Template{
		TemplateId: e.TemplateId,
		Name:       e.Name,
		Exercises:  toDomainExerciseTemplate(e.Exercises),
	}
}

func toDomainExerciseTemplate(e []models.ExerciseTemplateEntity) []domain.ExerciseIdTemplate {
	exercises := make([]domain.ExerciseIdTemplate, len(e))
	for i, v := range e {
		exercises[i] = domain.ExerciseIdTemplate{
			ExerciseId: v.ExerciseId,
			Name:       v.ExerciseId,
			Sets:       toDomainSets(v.Sets),
		}
	}

	return exercises
}

func toDomainSets(s []models.Set) []domain.Set {
	sets := make([]domain.Set, len(s))
	for i, v := range s {
		sets[i] = domain.Set{
			Rep:  v.Rep,
			Kg:   v.Kg,
			Type: domain.TypeSet(v.Type),
		}
	}
	return sets
}

/*
	Converts Domain -> Entity
	adds the userId
*/
func toEntityTemplate(d domain.Template, userId string) models.TemplateEntity {
	return models.TemplateEntity {
		TemplateId: d.TemplateId,
		UserId: userId,
		Name: d.Name,
		Exercises: toEntityExerciseTemplate(d.Exercises),
	}
}

func toEntityExerciseTemplate(e []domain.ExerciseIdTemplate) []models.ExerciseTemplateEntity {
	exercises := make([]models.ExerciseTemplateEntity, len(e))
	for i, v := range e {
		exercises[i] = models.ExerciseTemplateEntity{
			ExerciseId: v.ExerciseId,
			Name:       v.ExerciseId,
			Sets:       toEntitySets(v.Sets),
		}
	}

	return exercises
}

func toEntitySets(s []domain.Set) []models.Set {
	sets := make([]models.Set, len(s))
	for i, v := range s {
		sets[i] = models.Set{
			Rep:  v.Rep,
			Kg:   v.Kg,
			Type: models.TypeSet(v.Type),
		}
	}
	return sets
}

// SESSIONS (reusing the same functions)
func toDomainSession(e models.SessionEntity) domain.Session {
	return domain.Session {
		SessionId: e.SessionId,
		Name: e.Name,
		Exercises: toDomainExerciseTemplate(e.Exercises),
		Duration: e.Duration,
		Date: e.Date,
		Note: e.Note,
	}
}

func toEntitySession(d domain.Session, userId string) models.SessionEntity {
	return models.SessionEntity {
		SessionId: d.SessionId,
		UserId: userId,
		Name: d.Name,
		Exercises: toEntityExerciseTemplate(d.Exercises),
		Duration: d.Duration,
		Date: d.Date,
		Note: d.Note,
	}
}
