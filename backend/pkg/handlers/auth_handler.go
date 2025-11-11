package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
)

func genGoogleOauthConfig(clientId, ClientSecret string) *oauth2.Config {
	return &oauth2.Config{
		RedirectURL:  "http://localhost",
		ClientID:     clientId,
		ClientSecret: ClientSecret,
		Scopes:       []string{"https://www.googleapis.com/auth/userinfo.profile"},
		Endpoint:     google.Endpoint,
	}
}

func HandleAuth(cfg *config.Config) http.HandlerFunc {
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
		googleOauthConfig := genGoogleOauthConfig(cfg.GOOGLE_CLIENT_ID, cfg.GOOGLE_CLIENT_SECRET)
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

		name := userInfo["name"].(string)
		googleId := userInfo["id"].(string)
		log.Printf("name: %v\n", name)

		userId := utils.EnsureInDB(googleId, name)

		jwt, err := utils.CreateToken(userId, cfg.JWT_KEY)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		result := utils.AuthResponse{
			Token:  jwt,
			UserId: userId,
			Name:   name,
		}

		utils.Encode(w, http.StatusOK, result)
	}
}
