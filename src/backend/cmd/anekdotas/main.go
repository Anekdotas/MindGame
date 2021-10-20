package main

import (
	"anekdotas/internal/api"
	"anekdotas/internal/logic"
	"anekdotas/internal/repository/db"
	"fmt"
	"github.com/labstack/echo/v4"
	"os"
	"strconv"
)

func main() {
	e := echo.New()
	sqlRepo := db.New(db.NewDB(getDBCredentials()))
	newAPI := api.New(logic.New(sqlRepo))
	newAPI.BindQuestionsRoutes(e)
	e.Logger.Fatal(e.Start(":" + mustGetEnvVar("APP_PORT")))
}

func getDBCredentials() (host string, port int, username, password, dbName string) {
	host = mustGetEnvVar("DB_HOST")
	port, err := strconv.Atoi(mustGetEnvVar("DB_PORT"))
	if err != nil {
		panic(err)
	}
	username = mustGetEnvVar("DB_USER")
	password = mustGetEnvVar("DB_PASS")
	dbName = mustGetEnvVar("DB_NAME")
	return
}

func mustGetEnvVar(key string) (value string) {
	value, ok := os.LookupEnv(key)
	if !ok {
		panic(fmt.Sprintf("%s is not set", key))
	}
	return
}
