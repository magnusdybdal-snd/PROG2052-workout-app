package utils

import (
	"time"

	"github.com/golang-jwt/jwt/v5"
)

type AuthRequest struct {
	Code string `json:"code"`
}

type AuthResponse struct {
	Token  string `json:"token"`
	UserId string `json:"userId"`
}

func CreateToken(userId string) (string, error) {
	claims := jwt.MapClaims{
		"user_id": userId,
		"exp":     time.Now().Add(1 * time.Minute).Unix(),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenStr, err := token.SignedString(token)
	if err != nil {
		return "", err
	}
	return tokenStr, nil
}

func EnsureInDB(googleId, email string) string {
	// quary the database, and return user id

	return googleId
}
