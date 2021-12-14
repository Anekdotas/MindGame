package handlers

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
	"net/http"
	"strconv"

	"github.com/labstack/echo/v4"
)

type Question struct {
	ID            int64     `json:"id,omitempty" query:"id"`
	Text          string    `json:"text"`
	MediaURL      string    `json:"mediaUrl,omitempty"`
	CorrectAnswer int64     `json:"correctAnswer"`
	Answers       []*Answer `json:"answers"`
}

func (h *Handlers) GetQuestions(c echo.Context) error {
	topic := c.Param("topic")
	if topic == "" {
		return echo.NewHTTPError(http.StatusBadRequest, "topic is empty")
	}
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	gameSessionID, questions, err := h.logic.GetQuestionsByTopic(c.Request().Context(), topic, userID)
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, echo.Map{
		"gameSessionId": gameSessionID,
		"questions": deriveFmapQuestions(func(q *anekdotas.Question) *Question {
			return &Question{
				ID:            q.ID,
				Text:          q.Text,
				MediaURL:      q.MediaURL,
				CorrectAnswer: q.CorrectAnswerID,
				Answers: deriveFmapAnswers(func(a *anekdotas.Answer) *Answer {
					return &Answer{
						ID:   a.ID,
						Text: a.Text,
					}
				}, q.Answers),
			}
		}, questions),
	})
}

func (h *Handlers) CreateQuestion(c echo.Context) error {
	question := new(Question)
	if err := c.Bind(question); err != nil {
		return err
	}
	topic := c.Param("topic")
	id, err := h.logic.CreateQuestionWithAnswers(
		c.Request().Context(),
		topic,
		&anekdotas.Question{
			Text:            question.Text,
			CorrectAnswerID: question.CorrectAnswer,
		},
		deriveFmapAnswersReverse(func(a *Answer) *anekdotas.Answer {
			return &anekdotas.Answer{
				Text: a.Text,
			}
		}, question.Answers),
	)
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, map[string]interface{}{"id": id})
}

func (h *Handlers) UploadMedia(c echo.Context) error {
	questionID, err := strconv.Atoi(c.Param("questionId"))
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
