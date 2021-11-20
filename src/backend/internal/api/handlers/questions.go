package handlers

import (
	"anekdotas"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

type Question struct {
	ID            int64    `json:"id,omitempty" query:"id" param:"id"`
	Text          string   `json:"text"`
	MediaURL      string   `json:"mediaUrl,omitempty"`
	CorrectAnswer int      `json:"correctAnswer"`
	Answers       []string `json:"answers"`
}

func (h *Handlers) GetQuestions(c echo.Context) error {
	topic := c.Param("topic")
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

func (h *Handlers) CreateQuestion(c echo.Context) error {
	question := new(Question)
	if err := c.Bind(question); err != nil {
		return err
	}
	topic := c.Param("topic")
	id, err := h.logic.CreateQuestion(c.Request().Context(), topic, &anekdotas.Question{
		Text:          question.Text,
		CorrectAnswer: question.CorrectAnswer,
		Answers:       question.Answers,
	})
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, map[string]interface{}{"id": id})
}

func (h *Handlers) UploadMedia(c echo.Context) error {
	questionID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	file, err := c.FormFile("media")
	if err != nil {
		c.Logger().Error()
		return err
	}
	src, err := file.Open()
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	defer src.Close()
	if err = h.logic.SaveMediaFile(c.Request().Context(), int64(questionID), file.Filename, src); err != nil {
		c.Logger().Error(err)
		return err
	}
	return nil
}
