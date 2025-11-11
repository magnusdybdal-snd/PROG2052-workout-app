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
	Name   string `json:"name"`
}

func CreateToken(userId, secret string) (string, error) {
	claims := jwt.MapClaims{
		"user_id": userId,
		"exp":     time.Now().Add(1000000 * time.Hour).Unix(),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenStr, err := token.SignedString([]byte(secret))
	if err != nil {
		return "", err
	}
	return tokenStr, nil
}

func EnsureInDB(googleId, name string) string {
	// quary the database, and return user id

	return googleId
}
