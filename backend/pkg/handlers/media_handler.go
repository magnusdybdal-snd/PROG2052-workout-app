package handlers

import (
	"net/http"
	"os"
	"path/filepath"
)

func HandleMedia() http.Handler {
	wd,_:= os.Getwd()
	return http.FileServer(http.Dir(filepath.Join(wd,"assets/exercises")))
}
