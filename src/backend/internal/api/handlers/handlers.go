package handlers

import (
	"anekdotas/internal/logic"
)

type Handlers struct {
	logic *logic.Logic
}

func New(logic *logic.Logic) *Handlers {
	return &Handlers{logic: logic}
}
