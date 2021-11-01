package api

import (
	"anekdotas"
	"anekdotas/internal/logic"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

type handlers struct {
	logic *logic.Logic
}

func newHandlers(logic *logic.Logic) *handlers {
	return &handlers{logic: logic}
}

func (h *handlers) GetQuestions(c echo.Context) error {
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

func (h *handlers) CreateQuestion(c echo.Context) error {
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

func (h *handlers) UploadMedia(c echo.Context) error {
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

func (h *handlers) GetTopics(c echo.Context) error {
	categoryID, err := strconv.Atoi(c.Param("category"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Category ID")
	}
	topics, err := h.logic.GetTopicsByCategory(c.Request().Context(), int64(categoryID))
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

func (h *handlers) CreateTopic(c echo.Context) error {
	categoryID, err := strconv.Atoi(c.Param("category"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Category ID")
	}
	topic := new(Topic)
	if err := c.Bind(topic); err != nil {
		return err
	}
	name, err := h.logic.CreateTopic(c.Request().Context(), int64(categoryID), &anekdotas.Topic{
		Name:   topic.Name,
		Author: topic.Author,
		// TODO: AN-36 - add question_per_game
	})
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, map[string]interface{}{"name": name})
}

func (h *handlers) GetCategories(c echo.Context) error {
	categories, err := h.logic.GetAllCategories(c.Request().Context())
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, deriveFmapCategories(func(cat *anekdotas.Category) *Category {
		return &Category{
			ID:   cat.ID,
			Name: cat.Name,
		}
	}, categories))
}
