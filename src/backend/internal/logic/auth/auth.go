package auth

import (
	"errors"
	"strconv"
	"time"

	"github.com/golang-jwt/jwt"
	"github.com/labstack/echo/v4"
)

type AuthProvider interface {
	NewToken(userID int64) (token string, err error)
}

type JWTAuth struct {
	sharedSecret []byte
}

func NewJWTAuth(sharedSecret string) *JWTAuth {
	return &JWTAuth{
		sharedSecret: []byte(sharedSecret),
	}
}

func (a *JWTAuth) NewToken(userID int64) (string, error) {
	claims := jwt.StandardClaims{
		Subject:   strconv.Itoa(int(userID)),
		ExpiresAt: time.Now().Add(time.Hour * 24).Unix(),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString(a.sharedSecret)
}

func (a *JWTAuth) ParseJWT(tokenStr string, c echo.Context) (interface{}, error) {
	token, err := jwt.Parse(tokenStr, func(t *jwt.Token) (interface{}, error) {
		return a.sharedSecret, nil
	})
	if err != nil {
		return nil, err
	}
	if !token.Valid {
		return nil, errors.New("invalid token")
	}
	return token, nil
}

func GetUserIDFromToken(t interface{}) (int64, error) {
	token, ok := t.(*jwt.Token)
	if !ok {
		return 0, errors.New("invalid token type")
	}
	var subject string
	switch token.Claims.(type) {
	case jwt.MapClaims:
		subject = token.Claims.(jwt.MapClaims)["sub"].(string)
	case jwt.StandardClaims:
		subject = token.Claims.(jwt.StandardClaims).Subject
	}
	userID, err := strconv.Atoi(subject)
	return int64(userID), err
}
