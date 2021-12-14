package handlers

import (
	"anekdotas"
	"net/http"

	"github.com/labstack/echo/v4"
	"github.com/pkg/errors"
)

type User struct {
	ID             int64  `json:"id,omitempty" query:"id" param:"id"`
	Username       string `json:"username"`
	Email          string `json:"email"`
	Password       string `json:"password,omitempty"`
	RepeatPassword string `json:"repeatPassword,omitempty"`
}

func (h *Handlers) RegisterUser(c echo.Context) error {
	user := new(User)
	if err := c.Bind(user); err != nil {
		c.Logger().Error(err)
		return err
	}
	if user.Password != user.RepeatPassword {
		return c.String(http.StatusBadRequest, "Fields password and repeat_password do not match")
	}
	id, err := h.logic.RegisterUser(c.Request().Context(), &anekdotas.User{
		Username: user.Username,
		Email:    user.Email,
	}, user.Password)
	if err != nil {
		if errors.Is(err, anekdotas.ErrAlreadyExists) {
			return c.String(http.StatusBadRequest, "User with that username already exists")
		}
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusCreated, echo.Map{
		"id": id,
	})
}

func (h *Handlers) Login(c echo.Context) error {
	user := new(User)
	if err := c.Bind(user); err != nil {
		c.Logger().Error(err)
		return err
	}
	token, err := h.logic.AuthenticateUser(c.Request().Context(), &anekdotas.User{
		Username: user.Username,
		Email:    user.Email,
	}, user.Password)
	if err != nil {
		if errors.Is(err, anekdotas.ErrIncorrectPassword) || errors.Is(err, anekdotas.ErrNotFound) {
			return c.String(http.StatusBadRequest, "Incorrect username or password")
		}
		c.Logger().Error(err)
		return echo.ErrInternalServerError
	}
	return c.JSON(http.StatusOK, echo.Map{
		"token": token,
	})
}
