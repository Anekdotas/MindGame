package handlers

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
	"errors"
	"net/http"
	"time"

	"github.com/labstack/echo/v4"
)

type choice struct {
	QuestionsID int64 `json:"questionId"`
	AnswerID    int64 `json:"answerId"`
}

type Statistics struct {
	ID        int64     `json:"gameSessionId"`
	Choices   []*choice `json:"choices"`
	TimeSpent int       `json:"timeSpent"`
	Streak    uint16    `json:"streak"`
}

func (h *Handlers) FinishGameSession(c echo.Context) error {
	statistics := new(Statistics)
	if err := c.Bind(statistics); err != nil {
		c.Logger().Error(err)
		return err
	}
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	if err := h.logic.FinishGameSession(
		c.Request().Context(),
		userID,
		&anekdotas.GameAnalytics{
			GameSessionID: statistics.ID,
			Choices:       convertChoices(statistics.Choices),
			TimeSpent:     time.Second * time.Duration(statistics.TimeSpent),
			Streak:        statistics.Streak,
		},
	); err != nil {
		if errors.Is(err, anekdotas.ErrNotFound) || errors.Is(err, anekdotas.ErrAlreadyExists) ||
			errors.Is(err, anekdotas.ErrQuestionsAndAnswersMismatch) {
			return c.String(http.StatusBadRequest, "Statistics data are invalid or missing")
		}
		if errors.Is(err, anekdotas.ErrGameSessionAlreadyFinished) {
			return c.String(http.StatusBadRequest, "Game Session is already finished")
		}
		c.Logger().Error(err)
		return err
	}
	return nil
}

func convertChoices(choices []*choice) []*anekdotas.Choice {
	return deriveFmapChoicesReverse(
		func(ch *choice) *anekdotas.Choice {
			return &anekdotas.Choice{
				QuestionID: ch.QuestionsID,
				AnswerID:   ch.AnswerID,
			}
		}, choices,
	)
}
