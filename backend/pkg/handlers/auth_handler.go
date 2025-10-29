package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
)

var googleOauthConfig = &oauth2.Config{
    RedirectURL:  "com.yourapp:/oauth2redirect",
    ClientID:     os.Getenv("GOOGLE_CLIENT_ID"),
    ClientSecret: os.Getenv("GOOGLE_CLIENT_SECRET"),
    Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile"},
    Endpoint:     google.Endpoint,
}

type AuthRequest struct {
	Code string `json:"code"`
}

type AuthResponse struct {
	Token  string `json:"token"`
	UserId string `json:"userId"`
}

func createToken(userId string) (string, error) {
	claims := jwt.MapClaims{
		"user_id": userId,
		"exp":     time.Now().Add(24 * time.Hour).Unix(),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenStr, err := token.SignedString(token)
	if err != nil {
		return "", err
	}
	return tokenStr, nil
}

func verifyToken(tokenString string, cfg *config.Config) error {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		return cfg.JWT_KEY, nil
	})
	if err != nil {
		return err
	}
	if !token.Valid {
		return fmt.Errorf("invalid token")
	}
	return nil
}

func ensureInDB(googleId, email string) string {
	// quary the database, and return user id

	return googleId
}

func HandleAuth() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(),5*time.Second)
		defer cancel()
		if r.Method != http.MethodPost {
			utils.HandleError(w, http.StatusMethodNotAllowed, fmt.Errorf("bad method"), utils.ErrMsgNotAllowed)
			return
		}
		payload, err := utils.Decode[AuthRequest](r)
		if err != nil {
			utils.HandleError(w, http.StatusBadRequest, err, utils.ErrMsgBadRequest)
			return
		}
		token, err := googleOauthConfig.Exchange(ctx,payload.Code)
		if err != nil {
			utils.HandleError(w,http.StatusBadRequest, err,utils.ErrMsgBadRequest)
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

		email := userInfo["emaol"].(string)
		googleId := userInfo["id"].(string)

		userId := ensureInDB(googleId, email)
		jwt, err := createToken(userId)
		if err != nil {
			utils.HandleError(w, http.StatusInternalServerError, err, utils.ErrMsgInternal)
			return
		}
		result := AuthResponse {
			Token: jwt,
			UserId: userId,
		}
		utils.Encode(w,http.StatusOK,result)		
	}
}
