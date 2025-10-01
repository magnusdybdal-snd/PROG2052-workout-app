package utils

import "net/http"

func ParseInclude(r *http.Request, val string) bool {
	includeStr := r.URL.Query().Get("include")
	switch includeStr {
	case "":
		return false 
	case val:
		return true
	default:
		return false
	}
}
