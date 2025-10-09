package main

import (
	"context"
	"fmt"
	"os"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/pkg/api"
)

func main()  {
	ctx := context.Background()
	if err := api.Run(ctx,os.Stdout, os.Args); err != nil {
		fmt.Fprintf(os.Stderr, "Server exited with error: %v\n", err)
		os.Exit(1)
	}
}
