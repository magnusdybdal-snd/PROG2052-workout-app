package handlers

import (
	"fmt"
	"net/http"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
)

func NotFound() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		utils.HandleError(w,http.StatusNotFound,fmt.Errorf(utils.ErrMsgNotFound),utils.ErrMsgNotFound)
	}
}
