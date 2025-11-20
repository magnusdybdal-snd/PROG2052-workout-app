package handlers

import (
	"fmt"
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
)

// HandleHome returns API version and available endpoints
func HandleHome() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w,
				http.StatusMethodNotAllowed,
				fmt.Errorf("method not allowed"),
				"Method not allowed")
			return
		}

		resp := map[string]interface{}{
			"version":     "v1",
			"description": "Welcome to the API",
			"endpoints": []string{
				"GET /api/v1/",
				"GET /api/v1/exercises",
				"GET /api/v1/exercises/{exerciseId}",
				"GET /api/v1/templates",
				"GET /api/v1/templates/{templateId}",
				"GET /api/v1/sessions",
				"POST /api/v1/sessions",
				"DELETE /api/v1/sessions/{sessionId}",
				"POST /api/v1/auth/google",
				"GET /api/v1/media/",
			},
		}

		utils.Encode(w, http.StatusOK, resp)
	}
}
