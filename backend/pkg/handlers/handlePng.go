package handlers

import (
	"image/png"
	"net/http"
	"os"
)


/*
* ONLY FOR TESTING PURPOSE
*/
func HandlePng(filename string) http.HandlerFunc {
	return func (w http.ResponseWriter, r *http.Request)  {
		f, _ := os.Open("assets/exercises/" + filename)
		defer f.Close()

		img, _ := png.Decode(f)

		w.Header().Set("Content-Type", "image/png")
		png.Encode(w,img)

	}
}
