package handlers

import (
	"fmt"
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
)

func HandleHome() http.HandlerFunc {
	type response struct {
		Version   string
		Resources []string
	}
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			utils.HandleError(w,
				http.StatusMethodNotAllowed,
				fmt.Errorf("method not allowed"),
				"Method not allowed")
		}
		resp := response{
			Version: "v1",
			Resources: []string {
				"hei", "hallo", "halla", 
			},
		}

		utils.Encode(w, http.StatusOK, resp)
	}
}
