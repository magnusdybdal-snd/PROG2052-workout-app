package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	Host                 string
	HOST_URL             string
	Port                 string
	UriDB                string
	Mode                 string
	JWT_KEY              string
	GOOGLE_CLIENT_ID     string
	GOOGLE_CLIENT_SECRET string
}

func LoadConfig() *Config {
	err := godotenv.Load()
	if err != nil {
		log.Println("No .env file present")
	}

	host := os.Getenv("HOST")
	if host == "" {
		host = "0.0.0.0"
	}

	host_url := os.Getenv("HOST_URL")
	if host_url == "" {
		log.Fatal("Missing HOST_URL")
	}

	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	UriDb := os.Getenv("MONGO_URI")
	if UriDb == "" {
		log.Fatal("MONGO_URI is required but not set")
	}
	mode := os.Getenv("MODE")
	if mode == "" {
		mode = "PRODUCTION"
	}
	key := os.Getenv("JWT_KEY")
	if key == "" {
		log.Fatal("JWT_KEY not found")
	}

	clientId := os.Getenv("GOOGLE_CLIENT_ID")
	if clientId == "" {
		log.Fatal("No client id found")
	}

	clientSecret := os.Getenv("GOOGLE_CLIENT_SECRET")
	if clientSecret == "" {
		log.Fatal("no client secret found")
	}

	return &Config{
		Host:                 host,
		HOST_URL:             host_url,
		Port:                 port,
		UriDB:                UriDb,
		Mode:                 mode,
		JWT_KEY:              key,
		GOOGLE_CLIENT_SECRET: clientSecret,
		GOOGLE_CLIENT_ID:     clientId,
	}
}
