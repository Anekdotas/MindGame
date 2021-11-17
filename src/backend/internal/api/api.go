package api

import (
	"anekdotas/internal/api/handlers"
	"anekdotas/internal/logic"
)

type API struct {
	handlers *handlers.Handlers
}

func New(logic *logic.Logic) *API {
	return &API{
		handlers: handlers.New(logic),
	}
}
