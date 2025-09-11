package api

// Routes
const (
	INDEX = "/"
	VERSION = "v1"
	API_ROUTE = "/api/" + VERSION 
)

// middleware keys
type contextKey string
const DBKEY contextKey = "db"
