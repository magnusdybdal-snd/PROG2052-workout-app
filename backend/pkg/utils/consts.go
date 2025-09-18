package utils

const (
	// Client-side errors (4xx)
	ErrMsgNotAllowed      = "method not allowed"
	ErrMsgBadRequest      = "invalid request"
	ErrMsgUnauthorized    = "unauthorized"
	ErrMsgForbidden       = "forbidden"
	ErrMsgNotFound        = "resource not found"
	ErrMsgConflict        = "conflict occurred"
	ErrMsgUnprocessable   = "unprocessable entity"

	// Server-side errors (5xx)
	ErrMsgInternal        = "internal server error"
	ErrMsgServiceUnavailable = "service temporarily unavailable"
	ErrMsgGatewayTimeout  = "gateway timeout"
)
