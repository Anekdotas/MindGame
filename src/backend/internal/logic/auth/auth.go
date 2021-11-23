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
