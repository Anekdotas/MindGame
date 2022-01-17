package handlers

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
	"errors"
	"net/http"

	"github.com/labstack/echo/v4"
)

func (h *Handlers) GetPurchasedHosts(c echo.Context) error {
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	ids, err := h.logic.GetPurchasedHosts(c.Request().Context(), userID)
	if err != nil {
		c.Logger().Error(err)
		return err
	}
	return c.JSON(http.StatusOK, echo.Map{
		"ids": ids,
	})
}

func (h *Handlers) PurchaseHost(c echo.Context) error {
	userID, err := auth.GetUserIDFromToken(c.Get("user"))
	if err != nil {
		return err
	}
	req := new(struct {
		HostID int64 `json:"hostId"`
	})
	if err := c.Bind(req); err != nil {
		return c.String(http.StatusBadRequest, "Invalid request body")
	}
	if err := h.logic.PurchaseHost(c.Request().Context(), req.HostID, userID); err != nil {
		if errors.Is(err, anekdotas.ErrAlreadyExists) {
			return c.String(http.StatusBadRequest, "Host is already purchased")
		}
		if errors.Is(err, anekdotas.ErrNotFound) {
			return c.String(http.StatusBadRequest, "Host is not found")
		}
		c.Logger().Error(err)
		return err
	}
	return nil
}
