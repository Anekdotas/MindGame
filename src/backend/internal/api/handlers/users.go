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
	TotalTimeSpent           uint16  `json:"totalTimeSpent,omitempty"`
	CorrectAnswers           uint16  `json:"correctAnswers,omitempty"`
	CorrectAnswersPercentage float32 `json:"correctAnswersPercentage,omitempty"`
	LongestStreak            uint16  `json:"longestStreak,omitempty"`
	LongestStreakTopicID     int64   `json:"longestStreakTopicId,omitempty"`
	AverageGameTime          uint16  `json:"averageGameTime,omitempty"`
	TopicsCreated            uint16  `json:"topicsCreated,omitempty"`
	TopicsRated              uint16  `json:"topicsRated,omitempty"`
	TopicsPlayed             uint16  `json:"topicsPlayed,omitempty"`
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
		TotalTimeSpent:           uint16(stats.TotalTimeSpent.Seconds()),
		CorrectAnswers:           stats.CorrectAnswers,
		CorrectAnswersPercentage: stats.CorrectAnswersPercentage,
		LongestStreak:            stats.LongestStreak,
		LongestStreakTopicID:     stats.LongestStreakTopicID,
		AverageGameTime:          uint16(stats.AverageGameTime.Seconds()),
		TopicsCreated:            stats.TopicsCreated,
		TopicsRated:              stats.TopicsRated,
		TopicsPlayed:             stats.TopicsPlayed,
	})
}
