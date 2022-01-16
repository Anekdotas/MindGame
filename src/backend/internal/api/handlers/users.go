package handlers

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
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

type UserStatistics struct {
	Coins                    uint    `json:"coins"`
	TotalTimeSpent           uint16  `json:"totalTimeSpent"`
	CorrectAnswers           uint16  `json:"correctAnswers"`
	CorrectAnswersPercentage float32 `json:"correctAnswersPercentage"`
	LongestStreak            uint16  `json:"longestStreak"`
	LongestStreakTopic       string  `json:"longestStreakTopic"`
	AverageGameTime          uint16  `json:"averageGameTime"`
	TopicsCreated            uint16  `json:"topicsCreated"`
	TopicsRated              uint16  `json:"topicsRated"`
	TopicsPlayed             uint16  `json:"topicsPlayed"`
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

func (h *Handlers) GetStats(c echo.Context) error {
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	stats, err := h.logic.GetUserStats(c.Request().Context(), userID)
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, &UserStatistics{
		Coins:                    stats.Coins,
		TotalTimeSpent:           uint16(stats.TotalTimeSpent.Seconds()),
		CorrectAnswers:           stats.CorrectAnswers,
		CorrectAnswersPercentage: stats.CorrectAnswersPercentage,
		LongestStreak:            stats.LongestStreak,
		LongestStreakTopic:       stats.LongestStreakTopic,
		AverageGameTime:          uint16(stats.AverageGameTime.Seconds()),
		TopicsCreated:            stats.TopicsCreated,
		TopicsRated:              stats.TopicsRated,
		TopicsPlayed:             stats.TopicsPlayed,
	})
}

func (h *Handlers) UpdateCoins(c echo.Context) error {
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	req := new(struct {
		CoinsDelta int `json:"coins"`
	})
	if err := c.Bind(req); err != nil {
		return c.String(http.StatusBadRequest, "Invalid request body")
	}
	if err := h.logic.UpdateUserCoins(c.Request().Context(), userID, req.CoinsDelta); err != nil {
		if errors.Is(err, anekdotas.ErrNegativeCoinsValue) {
			return c.String(http.StatusBadRequest, "Negative coins result value is not permitted")
		}
		c.Logger().Error(err)
		return err
	}
	return nil
}
