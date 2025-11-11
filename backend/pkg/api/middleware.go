package api

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api/config"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/utils"
	"go.uber.org/zap"
)

/*
Top level Middleware
used in all endpoints
*/
func CorsMiddleware() func(h http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			w.Header().Set("Access-Control-Allow-Origin", "*")
			w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
			w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
			w.Header().Set("Content-type", "application/json")

			if r.Method == http.MethodOptions {
				w.WriteHeader(http.StatusOK)
				return
			}
			next.ServeHTTP(w, r)
		})
	}
}

// Top level logging middleware
func LoggingMiddleware(logger *zap.Logger) func(h http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			duration := time.Since(start)
			next.ServeHTTP(w, r)
			logger.Info("HTTP Request",
				zap.String("method", r.Method),
				zap.String("url", r.URL.Path),
				zap.Duration("duration", duration),
				zap.String("remote_addr", r.RemoteAddr))
		})
	}
}

// Middleware to authenticate the user
func AuthenticateUser(cfg *config.Config, next http.HandlerFunc) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			utils.HandleError(w, http.StatusUnauthorized, fmt.Errorf("missing token"), utils.ErrMsgUnauthorized)
			return
		}
		tokenStr := authHeader[len("Bearer "):]
		token, _ := jwt.Parse(tokenStr, func(token *jwt.Token) (interface{}, error) {
			return []byte(cfg.JWT_KEY), nil
		})
		if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
			log.Println(claims)
			userId := claims["user_id"].(string)
			ctx := context.WithValue(r.Context(), "userId", userId)
			log.Println("successfully authenticated")
			next(w, r.WithContext(ctx))
		} else {
			utils.HandleError(w, http.StatusUnauthorized, fmt.Errorf("invalid token"), utils.ErrMsgUnauthorized)
		}
	})
}
