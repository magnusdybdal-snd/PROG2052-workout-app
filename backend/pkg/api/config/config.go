package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	Host string
	Port string
	UriDB string 
	Mode string
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
	
	UriDb := os.Getenv("MONGO_URI")
	if port == "" {
		log.Fatal("MONGO_URI is required but not set")
	}
	mode := os.Getenv("MODE")
	if mode == "" {
		mode = "PRODUCTION"
	}

	return &Config{
		Host: host,
		Port: port,
		UriDB: UriDb,
		Mode: mode,
	}
}
