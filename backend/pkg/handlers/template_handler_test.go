package handlers_test

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"testing"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/handlers"
)

type mockTemplateService struct {
	GetAllFunc func(ctx context.Context, userId string, include bool) (interface{}, error)
	GetOneFunc func(ctx context.Context, id string, include bool) (interface{}, error)
	CreateFunc func(ctx context.Context, payload *domain.Template) (string, error)
	UpdateFunc func(ctx context.Context, id string, payload interface{}) (string, error)
	DeleteFunc func(ctx context.Context, id string) (string, error)
}

func (m *mockTemplateService) GetAll(ctx context.Context, userId string, include bool) (interface{}, error) {
	return m.GetAllFunc(ctx, userId, include)
}

func (m *mockTemplateService) GetOne(ctx context.Context, id string, include bool) (interface{}, error) {
	return m.GetOneFunc(ctx, id, include)
}

func (m *mockTemplateService) Create(ctx context.Context, payload *domain.Template) (string, error) {
	return m.CreateFunc(ctx, payload)
}

func (m *mockTemplateService) Update(ctx context.Context, id string, payload interface{}) (string, error) {
	return m.UpdateFunc(ctx, id, payload)
}

func (m *mockTemplateService) Delete(ctx context.Context, id string) (string, error) {
	return m.DeleteFunc(ctx, id)
}

func TestHandleTemplate_GetAll(t *testing.T) {
	mockSvc := &mockTemplateService{
		GetAllFunc: func(ctx context.Context, userId string, include bool) (interface{}, error) {
			return []domain.Template{
				{TemplateId: "tmp_001", Name: "Upper Body"},
				{TemplateId: "tmp_002", Name: "Lower Body"},
			}, nil
		},
	}

	req := httptest.NewRequest(http.MethodGet, "/template", nil)
	w := httptest.NewRecorder()

	handler := handlers.HandleTemplate(mockSvc)
	handler(w, req)

	if w.Code != http.StatusOK {
		t.Fatalf("expected status 200, got %d", w.Code)
	}

	var templates []domain.Template
	if err := json.Unmarshal(w.Body.Bytes(), &templates); err != nil {
		t.Fatalf("failed to decode response: %v", err)
	}

	if len(templates) != 2 {
		t.Errorf("expected 2 templates, got %d", len(templates))
	}
}

func TestHandleOneTemplate_GetOne(t *testing.T) {
	mockSvc := &mockTemplateService{
		GetOneFunc: func(ctx context.Context, id string, include bool) (interface{}, error) {
			return domain.Template{TemplateId: id, Name: "Upper Body"}, nil
		},
	}

	req := httptest.NewRequest(http.MethodGet, "/template/tmp_001", nil)
	req.SetPathValue("templateId", "tmp_001")
	w := httptest.NewRecorder()

	handler := handlers.HandleOneTemplate(mockSvc)
	handler(w, req)

	if w.Code != http.StatusOK {
		t.Fatalf("expected 200, got %d", w.Code)
	}

	var tpl domain.Template
	if err := json.Unmarshal(w.Body.Bytes(), &tpl); err != nil {
		t.Fatalf("failed to unmarshal: %v", err)
	}

	if tpl.TemplateId != "tmp_001" {
		t.Errorf("expected tmp_001, got %s", tpl.TemplateId)
	}
}

func TestHandleTemplate_Post(t *testing.T) {
	mockSvc := &mockTemplateService{
		CreateFunc: func(ctx context.Context, payload *domain.Template) (string, error) {
			if payload.Name == "" {
				return "", errors.New("missing name")
			}
			return "new_id", nil
		},
	}

	template := domain.Template{
		Name: "Full Body",
		Exercises: []domain.ExerciseIdTemplate{
			{ExerciseId: "JrOHAZc"},
		},
	}
	body, _ := json.Marshal(template)

	req := httptest.NewRequest(http.MethodPost, "/template", bytes.NewReader(body))
	w := httptest.NewRecorder()

	handler := handlers.HandleTemplate(mockSvc)
	handler(w, req)

	if w.Code != http.StatusOK {
		t.Fatalf("expected status 200, got %d", w.Code)
	}

	var result map[string]string
	if err := json.Unmarshal(w.Body.Bytes(), &result); err != nil {
		t.Fatalf("failed to decode response: %v", err)
	}

	if result["id"] != "new_id" {
		t.Errorf("expected id=new_id, got %s", result["id"])
	}
}

func TestHandleOneTemplate_Delete(t *testing.T) {
	mockSvc := &mockTemplateService{
		DeleteFunc: func(ctx context.Context, id string) (string, error) {
			if id == "missing" {
				return "", errors.New("not found")
			}
			return id, nil
		},
	}

	req := httptest.NewRequest(http.MethodDelete, "/template/tmp_001", nil)
	req.SetPathValue("templateId", "tmp_001")
	w := httptest.NewRecorder()

	handler := handlers.HandleOneTemplate(mockSvc)
	handler(w, req)

	if w.Code != http.StatusOK {
		t.Fatalf("expected 200, got %d", w.Code)
	}

	var result map[string]string
	if err := json.Unmarshal(w.Body.Bytes(), &result); err != nil {
		t.Fatalf("failed to parse response: %v", err)
	}

	if result["id"] != "tmp_001" {
		t.Errorf("expected tmp_001, got %s", result["id"])
	}
}
