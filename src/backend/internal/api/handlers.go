package api

import (
	"anekdotas"
	"anekdotas/internal/logic"
	"github.com/labstack/echo/v4"
	"net/http"
)

type handlers struct {
	logic *logic.Logic
}

func newHandlers(logic *logic.Logic) *handlers {
	return &handlers{logic: logic}
}

func (h *handlers) GetQuestions(c echo.Context) error {
	topic := c.QueryParam("topic")
	if topic == "" {
		return echo.NewHTTPError(http.StatusBadRequest, "topic is empty")
	}
	questions, err := h.logic.GetQuestionsByTopic(c.Request().Context(), topic)
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, deriveFmapQuestions(func(q *anekdotas.Question) *Question {
		return &Question{
			ID:            q.ID,
			Text:          q.Text,
			MediaURL:      q.MediaURL,
			CorrectAnswer: q.CorrectAnswer,
			Answers:       q.Answers,
		}
	}, questions))
}

func (h *handlers) GetTopics(c echo.Context) error {
	topics, err := h.logic.GetAllTopics(c.Request().Context())
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, deriveFmapTopics(func(t *anekdotas.Topic) *Topic {
		return &Topic{
			ID:     t.ID,
			Name:   t.Name,
			Author: t.Author,
		}
	}, topics))
}
