package handlers

import (
	"fmt"
	"net/http"
)

func HelloAuth() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		userId := r.Context().Value("user_id").(string)
		fmt.Fprintf(w, "hello %s", userId)
	}
}
