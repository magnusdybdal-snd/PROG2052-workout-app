package main

import (
	"fmt"
	"log"
	"os"

	"github.com/joho/godotenv"
)

func main()  {
	if err := godotenv.Load(); err != nil {
		log.Fatal("Error loading .env file")
		return
	}
	testVar := os.Getenv("TEST")
	
	fmt.Println("Hello Backend")
	fmt.Println(testVar)
}
