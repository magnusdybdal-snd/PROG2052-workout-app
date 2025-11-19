package handlers

import (
	"log"
	"net/http"
	"os"
	"path/filepath"
)

func HandleMedia() http.Handler {
	exe,err := os.Executable()
	if err != nil {
		log.Fatal("Error finding os path")
	}
	base := filepath.Dir(exe)
	mediaPath := filepath.Join(base, "assets/exercises")
	return http.FileServer(http.Dir(mediaPath))
}
