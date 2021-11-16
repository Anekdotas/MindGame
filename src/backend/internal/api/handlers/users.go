package handlers

import (
	"anekdotas"
	"errors"
	"net/http"

	"github.com/labstack/echo/v4"
)

type User struct {
	ID       int64  `json:"id,omitempty" query:"id" param:"id"`
	Username string `json:"username"`
	Email    string `json:"email"`
	Password string `json:"password,omitempty"`
}

func (h *Handlers) Login(e echo.Context) error {
	user := new(User)
	if err := e.Bind(user); err != nil {
		return err
	}
	token, err := h.logic.AuthenticateUser(e.Request().Context(), &anekdotas.User{
		Username: user.Username,
		Email:    user.Email,
	}, user.Password)
	if err != nil {
		if errors.Is(err, anekdotas.ErrIncorrectPassword) || errors.Is(err, anekdotas.ErrNotFound) {
			return e.String(http.StatusBadRequest, "Incorrect username or password")
		}
		e.Logger().Error(err)
		return echo.ErrInternalServerError
	}
	return e.JSON(http.StatusOK, echo.Map{
		"token": token,
	})
}
