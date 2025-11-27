package handlers

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/domain"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
)

func HandleExampleTemplate(serv domain.ExampleTemplateService) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		if r.Method != http.MethodGet {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
		data, err := serv.GetAll(ctx)
		if err != nil {
			utils.HandleError(w,http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		utils.Encode(w,http.StatusOK,data)
	}
}
