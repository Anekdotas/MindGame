package handlers

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
	"errors"
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
	topics, err := h.logic.GetAllTopics(c.Request().Context(), int64(categoryID))
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

func (h *Handlers) GetRatedTopics(c echo.Context) error {
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	topics, err := h.logic.GetRatedTopics(c.Request().Context(), userID)
	if err != nil && !errors.Is(err, anekdotas.ErrNotFound) {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, echo.Map{
		"ids": deriveFmapUnpackTopics(func(t *anekdotas.Topic) int64 {
			return t.ID
		}, topics),
	})
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
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	name, err := h.logic.CreateTopic(c.Request().Context(), int64(categoryID), userID, &anekdotas.Topic{
		Name:        topic.Name,
		Description: topic.Description,
		Difficulty:  topic.Difficulty,
		// TODO: AN-36 - add question_per_game
	})
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, map[string]interface{}{"name": name})
}

func (h *Handlers) RateTopic(c echo.Context) error {
	_, err := strconv.Atoi(c.Param("categoryId"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Category ID")
	}
	topicID, err := strconv.Atoi(c.Param("topicId"))
	if err != nil {
		return c.String(http.StatusBadRequest, "Invalid Topic ID")
	}
	rating := new(struct {
		Rating float32 `json:"rating"`
	})
	if err := c.Bind(rating); err != nil {
		return c.String(http.StatusBadRequest, "Invalid request body")
	}
	if rating.Rating <= 0 || rating.Rating > 5 {
		return c.String(http.StatusBadRequest, "Invalid rating value, it must be between 0 (excl.) and 5 (incl.)")
	}
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	if err := h.logic.RateTopic(c.Request().Context(), userID, int64(topicID), rating.Rating); err != nil {
		if errors.Is(err, anekdotas.ErrAlreadyExists) {
			return c.String(http.StatusBadRequest, "Topic is already rated")
		}
		if errors.Is(err, anekdotas.ErrNotFound) {
			return c.String(http.StatusNotFound, "Topic does not exist")
		}
		c.Logger().Errorf("failed to rate topic: %v", err)
		return err
	}
	return nil
}
