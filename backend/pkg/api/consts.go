package api

// Routes
const (
	INDEX = "/"
	VERSION = "v1"
	API_ROUTE = "/api/" + VERSION 
	EXERCISES_ROUTE = API_ROUTE + "/exercises"
)

// middleware keys
type contextKey string
const DBKEY contextKey = "db"
