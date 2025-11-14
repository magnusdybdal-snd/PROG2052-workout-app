package handlers

import (
	"net/http"
	"os"
	"path/filepath"
)

func HandleMedia() http.Handler {
	exe, _ := os.Executable()
	base := filepath.Dir(exe)
	mediaPath := filepath.Join(base, "assets/exercises")
	return http.FileServer(http.Dir(mediaPath))
}
