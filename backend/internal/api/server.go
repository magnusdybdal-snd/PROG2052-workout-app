package api

import (
	"log"
	"net/http"
	"os"
)

type Server struct {
	srv *http.Server
}

func NewServer() *Server {
	mux := http.NewServeMux()
	mux.HandleFunc("/", func (w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
	})

	return &Server{
		srv: &http.Server{
			Addr: ":" + os.Getenv("PORT"),
			Handler: mux,
		},
	}
}

func (s *Server) StartServer() {
	log.Println("Starting server")
	log.Fatal(s.srv.ListenAndServe())

	s.srv.Close()
}
