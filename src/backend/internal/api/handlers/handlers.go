package handlers

import (
	"anekdotas"
	"anekdotas/internal/api"
	"anekdotas/internal/logic"
	"github.com/labstack/echo/v4"
	"net/http"
)

type Handlers struct {
	logic *logic.Logic
}

func New(logic *logic.Logic) *Handlers {
	return &Handlers{logic: logic}
}

func (h *Handlers) GetCategories(c echo.Context) error {
	categories, err := h.logic.GetAllCategories(c.Request().Context())
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, deriveFmapCategories(func(cat *anekdotas.Category) *api.Category {
		return &api.Category{
			ID:   cat.ID,
			Name: cat.Name,
		}
	}, categories))
}
