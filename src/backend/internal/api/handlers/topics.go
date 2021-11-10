package handlers

import (
	"anekdotas"
	"anekdotas/internal/api"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

func (h *Handlers) GetTopics(c echo.Context) error {
	categoryID, err := strconv.Atoi(c.Param("category"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Category ID")
	}
	topics, err := h.logic.GetTopicsByCategory(c.Request().Context(), int64(categoryID))
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, deriveFmapTopics(func(t *anekdotas.Topic) *api.Topic {
		return &api.Topic{
			ID:     t.ID,
			Name:   t.Name,
			Author: t.Author,
		}
	}, topics))
}

func (h *Handlers) CreateTopic(c echo.Context) error {
	categoryID, err := strconv.Atoi(c.Param("category"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Category ID")
	}
	topic := new(api.Topic)
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
