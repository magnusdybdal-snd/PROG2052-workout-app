package utils

import (
	"net/http"
	"strconv"
)

/*
	Takes a http request
	Checks the URL for ?limit=
	parses the result and return a INT
*/
func ParseLimit(r *http.Request, maxLimit int) int {
	limitStr := r.URL.Query().Get("limit")
	limit := maxLimit
	if val, err := strconv.Atoi(limitStr); err == nil && val > 0 && val <= maxLimit  {
		limit = val
	}
	
	return limit
}
