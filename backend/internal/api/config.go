package api

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	Host string
	Port string
}

func LoadConfig() *Config {
	err := godotenv.Load(); if err != nil {
		log.Println("No .env file present")
	}

	host := os.Getenv("HOST")
	if host == "" {
		host = "0.0.0.0"
	}

	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	return &Config{
		Host: host,
		Port: port,
	}
}
