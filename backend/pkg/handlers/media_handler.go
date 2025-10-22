package handlers

import (
	"fmt"
	"net/http"
	"os"
	"path/filepath"
)

func HandleMedia() http.Handler {
	wd,_:= os.Getwd()
	fmt.Println("Working dir: ", wd)
	return http.FileServer(http.Dir(filepath.Join(wd,"assets/exercises")))
}
