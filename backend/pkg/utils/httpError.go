package utils

import (
	"log"
	"net/http"
)
/**
 *  Error handler for printing to client and server
 *  @param w             - response writer
 *  @param statusCode    - Should be used with a appropiate error code
 *  @param err           - error which occurs, printed in client
*/
func HandleError(w http.ResponseWriter, code int, err error, msg string) {
	if err != nil {
		// server message
		log.Printf("ERROR: %v\n", err)
		// client message
		http.Error(w,msg,code)
	}
}
