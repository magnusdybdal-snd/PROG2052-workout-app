package main

import (
	"fmt"
	"os"

	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/internal/api"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/internal/db"
	"gitlab.stud.idi.ntnu.no/gruppe-1/prog2052-prosjekt/backend/utils"
)

func main()  {
	utils.LoadEnv()
	testVar := os.Getenv("TEST")
	
	fmt.Println("Hello Backend")
	fmt.Println(testVar)

	db.InitDB()

	app := api.NewServer()
	app.StartServer()
}
