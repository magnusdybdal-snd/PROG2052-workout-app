package api

import (
	"net/http"
	"time"

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
			next.ServeHTTP(w,r)
			logger.Info("HTTP Request",
				zap.String("method", r.Method),
				zap.String("url",r.URL.Path),
				zap.Duration("duration",duration),
				zap.String("remote_addr",r.RemoteAddr))
		})
	}
}

// Middleware to authenticate the user
func AuthenticateUser(h http.HandlerFunc) http.Handler {
	return http.HandlerFunc(func (w http.ResponseWriter, r *http.Request)  {
		
		h(w,r)
	})
}
