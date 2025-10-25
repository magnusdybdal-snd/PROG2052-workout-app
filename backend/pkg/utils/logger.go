package utils

import (
	"go.uber.org/zap"
)

func NewLogger(mode string) (*zap.Logger,error) {
	logger,err := zap.NewProduction()
	if err != nil {
		return nil, err
	}
	if mode == "DEVELOPMENT" {
		logger = zap.Must(zap.NewDevelopment())
	}

	logger.Info("Starting Logger")
	return logger,nil
}
