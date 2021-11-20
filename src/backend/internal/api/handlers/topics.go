package handlers

import (
	"anekdotas"
	"net/http"
	"strconv"

	"github.com/labstack/echo/v4"
)

type Topic struct {
	ID          int64   `json:"id,omitempty" query:"id" param:"id"`
	Name        string  `json:"name"`
	Description string  `json:"description"`
	Author      string  `json:"author"`
	Rating      float32 `json:"rating"`
	ImageURL    string  `json:"imageUrl,omitempty"`
	Difficulty  int     `json:"difficulty"`
}

func (h *Handlers) GetTopics(c echo.Context) error {
	categoryID, err := strconv.Atoi(c.Param("categoryId"))
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
			ID:          t.ID,
			Name:        t.Name,
			Description: t.Description,
			Author:      t.Author,
			ImageURL:    t.ImageURL,
			Difficulty:  t.Difficulty,
		}
	}, topics))
}

func (h *Handlers) CreateTopic(c echo.Context) error {
	categoryID, err := strconv.Atoi(c.Param("categoryId"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Category ID")
	}
	topic := new(Topic)
	if err := c.Bind(topic); err != nil {
		return err
	}
	name, err := h.logic.CreateTopic(c.Request().Context(), int64(categoryID), &anekdotas.Topic{
		Name:        topic.Name,
		Description: topic.Description,
		Author:      topic.Author,
		Difficulty:  topic.Difficulty,
		// TODO: AN-36 - add question_per_game
	})
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, map[string]interface{}{"name": name})
}
