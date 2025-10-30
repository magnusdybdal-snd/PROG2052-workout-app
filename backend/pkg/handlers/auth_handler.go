package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
)

var googleOauthConfig = &oauth2.Config{
	RedirectURL:  "com.example.workoutapp:/oauth2redirect",
	ClientID:     os.Getenv("GOOGLE_CLIENT_ID"),
	ClientSecret: os.Getenv("GOOGLE_CLIENT_SECRET"),
	Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile"},
	Endpoint:     google.Endpoint,
}

func HandleAuth(secret string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()
		if r.Method != http.MethodPost {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
		payload, err := utils.Decode[utils.AuthRequest](r)
		if err != nil {
			utils.HandleError(w, http.StatusBadRequest, err, utils.ErrMsgBadRequest)
			return
		}
		token, err := googleOauthConfig.Exchange(ctx, payload.Code)
		if err != nil {
			utils.HandleError(w, http.StatusBadRequest, err, utils.ErrMsgBadRequest)
			return
		}
		client := googleOauthConfig.Client(ctx, token)
		resp, err := client.Get("https://www.googleapis.com/oauth2/v2/userinfo")
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		defer resp.Body.Close()

		var userInfo map[string]interface{}
		json.NewDecoder(resp.Body).Decode(&userInfo)

		email := userInfo["email"].(string)
		googleId := userInfo["id"].(string)

		userId := utils.EnsureInDB(googleId, email)
		jwt, err := utils.CreateToken(userId,secret)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		result := utils.AuthResponse{
			Token:  jwt,
			UserId: userId,
		}
		utils.Encode(w, http.StatusOK, result)
	}
}
