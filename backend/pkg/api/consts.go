package api

// Routes
const (
	INDEX = "/"
	VERSION = "v1"
	API_ROUTE = "/api/" + VERSION 
	EXERCISES_ROUTE = API_ROUTE + "/exercises"
	EXERCISES_ID_ROUTE = EXERCISES_ROUTE + "/{exerciseId}"
	TEMPLATES_ROUTE = API_ROUTE + "/templates"
	TEMPLATES_ID_ROUTE = TEMPLATES_ROUTE + "/{templateId}"
	SESSIONS_ROUTE = API_ROUTE + "/sessions"

	MEDIA_ROUTE = API_ROUTE + "/media"
)

// middleware keys
type contextKey string
const DBKEY contextKey = "db"
