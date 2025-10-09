package utils

import (
	"log"
	"net/http"
)

type ErrorResponse struct {
    Error ErrorDetail `json:"error"`
}

type ErrorDetail struct {
    Code    int    `json:"code"`
    Message string `json:"message"`
}

/**
 *  Error handler for printing to client and server
 *  @param w             - response writer
 *  @param statusCode    - Should be used with a appropiate error code
 *  @param err           - error which occurs, printed in server
 *  @param msg           - error which occurs, printed in client 
*/
func HandleError(w http.ResponseWriter, code int, err error, msg string) {
    if err != nil {
        // Log error for server
        log.Printf("ERROR: %v\n", err)

        // Build JSON response
        resp := ErrorResponse{
            Error: ErrorDetail{
                Code:    code,
                Message: msg,
            },
        }

        Encode(w,code,resp)
    }
}


