package handlers_test

import (
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
)

type mockExerciseService struct {
	GetAllFunc func(ctx context.Context, limit int) ([]domain.Exercises, error)
	GetOneFunc func(ctx context.Context, id string) (domain.Exercises, error)
}

func (m *mockExerciseService) GetAll(ctx context.Context, limit int, url string) ([]domain.Exercises, error) {
	return m.GetAllFunc(ctx, limit)
}

func (m *mockExerciseService) GetOne(ctx context.Context, id string, url string) (domain.Exercises, error) {
	return m.GetOneFunc(ctx, id)
}

func TestGetAllExercises(t *testing.T) {
	mockSvc := &mockExerciseService{
		GetAllFunc: func(ctx context.Context, limit int) ([]domain.Exercises, error) {
			return []domain.Exercises{
				{
					ExerciseId:       "JrOHAZc",
					Name:             "Barbell Stiff Leg Good Morning",
					TargetMuscles:    []string{"Hamstrings", "Glutes"},
					BodyParts:        []string{"Lower Back", "Legs"},
					Equipments:       []string{"Barbell"},
					SecondaryMuscles: []string{"Erector Spinae", "Core"},
					GifUrl:           "https://example.com/gifs/barbell_good_morning.gif",
					Instructions: []string{
						"Stand with your feet shoulder-width apart holding a barbell on your upper back.",
						"Keep your legs slightly bent and hinge at the hips to lower your torso forward.",
						"Lower until you feel a stretch in your hamstrings, then return to the starting position.",
					},
				},
				{
					ExerciseId:       "7F1DVzn",
					Name:             "Lever Front Pulldown",
					TargetMuscles:    []string{"Latissimus Dorsi"},
					BodyParts:        []string{"Back"},
					Equipments:       []string{"Machine"},
					SecondaryMuscles: []string{"Biceps", "Rear Deltoids"},
					GifUrl:           "https://example.com/gifs/lever_front_pulldown.gif",
					Instructions: []string{
						"Sit down at a lat pulldown machine and grasp the bar with a wide grip.",
						"Pull the bar down to your chest, squeezing your shoulder blades together.",
						"Slowly return the bar to the starting position with control.",
					},
				},
				{
					ExerciseId:       "gAwDzB3",
					Name:             "Cable Triceps Pushdown (V-Bar)",
					TargetMuscles:    []string{"Triceps Brachii"},
					BodyParts:        []string{"Arms"},
					Equipments:       []string{"Cable Machine"},
					SecondaryMuscles: []string{"Shoulders"},
					GifUrl:           "https://example.com/gifs/cable_triceps_pushdown_vbar.gif",
					Instructions: []string{
						"Attach a V-bar to a high pulley and grab it with an overhand grip.",
						"Keep your elbows close to your body and push the bar down until your arms are fully extended.",
						"Return to the starting position with control.",
					},
				},
			}, nil
		},
	}

	req := httptest.NewRequest(http.MethodGet, "/exercises", nil)
	w := httptest.NewRecorder()

	handler := handlers.HandleExercises(mockSvc, "http://test.com")
	handler(w, req)

	if w.Code != http.StatusOK {
		t.Fatalf("expected status 200, got %d", w.Code)
	}

	var got []domain.Exercises
	if err := json.Unmarshal(w.Body.Bytes(), &got); err != nil {
		t.Fatalf("failed to decode body: %v", err)
	}

	if len(got) != 3 {
		t.Errorf("expected 3 exercises, got %d", len(got))
	}
}

func TestGetOneExercise(t *testing.T) {
	mockSvc := &mockExerciseService{
		GetOneFunc: func(ctx context.Context, id string) (domain.Exercises, error) {
			return domain.Exercises{
				ExerciseId:       "gAwDzB3",
				Name:             "Cable Triceps Pushdown (V-Bar)",
				TargetMuscles:    []string{"Triceps Brachii"},
				BodyParts:        []string{"Arms"},
				Equipments:       []string{"Cable Machine"},
				SecondaryMuscles: []string{"Shoulders"},
				GifUrl:           "https://example.com/gifs/cable_triceps_pushdown_vbar.gif",
				Instructions: []string{
					"Attach a V-bar to a high pulley and grab it with an overhand grip.",
					"Keep your elbows close to your body and push the bar down until your arms are fully extended.",
					"Return to the starting position with control.",
				},
			}, nil
		},
	}

	req := httptest.NewRequest(http.MethodGet, "/exercises/gAwDzB3", nil)
	req.SetPathValue("exerciseId", "gAwDzB3")
	w := httptest.NewRecorder()

	handler := handlers.HandleOneExercise(mockSvc, "http://test.com")
	handler(w, req)

	if w.Code != http.StatusOK {
		t.Fatalf("expected 200, got %d", w.Code)
	}

	var got domain.Exercises
	if err := json.Unmarshal(w.Body.Bytes(), &got); err != nil {
		t.Fatalf("failed to unmarshal response: %v", err)
	}

	if got.Name != "Cable Triceps Pushdown (V-Bar)" {
		t.Errorf("expected Deadlift, got %s", got.Name)
	}
}
