package auth

import (
	"strconv"
	"time"

	"github.com/golang-jwt/jwt"
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
		ExpiresAt: time.Now().Add(time.Minute * 5).Unix(),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString(a.sharedSecret)
}
