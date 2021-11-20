package handlers

import (
	"anekdotas"
	"github.com/labstack/echo/v4"
	"net/http"
)

type Category struct {
	ID   int64  `json:"id,omitempty" query:"id" param:"id"`
	Name string `json:"name"`
}

func (h *Handlers) GetCategories(c echo.Context) error {
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
