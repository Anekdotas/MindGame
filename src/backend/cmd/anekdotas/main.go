package main

import (
	"fmt"
	"os"
	"strconv"

	"github.com/golang-jwt/jwt"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"

	"anekdotas/internal/api"
	"anekdotas/internal/logic"
	"anekdotas/internal/logic/auth"
	"anekdotas/internal/repository/db"
)

func main() {
	e := echo.New()

	// Media storage initialization
	mediaDir := mustGetEnvVar("MEDIA_PATH")
	mediaURLPrefix := mustGetEnvVar("HOST_PREFIX")
	e.Static("/media", mediaDir)

	sqlRepo := db.New(db.NewDB(getDBCredentials()))
	defer sqlRepo.Close()
	authProvider := auth.NewJWTAuth(mustGetEnvVar("SECRET_KEY"))
	newLogic := logic.New(sqlRepo, authProvider, mediaDir, mediaURLPrefix)

	// HTTP API initialization
	newAPI := api.New(newLogic)
	jwtMiddleware := middleware.JWTWithConfig(middleware.JWTConfig{
		ParseTokenFunc: authProvider.ParseJWT,
		Claims:         &jwt.StandardClaims{},
	})
	newAPI.BindApiRoutes(e, jwtMiddleware)

	if err := e.StartTLS(
		":"+mustGetEnvVar("APP_PORT"), "certs/mindgame.crt", "certs/mindgame.key",
	); err != nil {
		e.Logger.Fatal(err)
	}
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
