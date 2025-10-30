package handlers

import (
	"fmt"
	"net/http"
)

func HelloAuth() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		userId := r.Context().Value("userId").(string)
		fmt.Fprintf(w, "hello %s", userId)
	}
}
