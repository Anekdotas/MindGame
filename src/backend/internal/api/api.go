package api

import (
	"anekdotas/internal/api/handlers"
	"anekdotas/internal/logic"
)

type Question struct {
	ID            int64    `json:"id,omitempty" query:"id" param:"id"`
	Text          string   `json:"text"`
	MediaURL      string   `json:"mediaUrl,omitempty"`
	CorrectAnswer int      `json:"correctAnswer"`
	Answers       []string `json:"answers"`
}

type Topic struct {
	ID     int64  `json:"id,omitempty" query:"id" param:"id"`
	Name   string `json:"name"`
	Author string `json:"author"`
}

type Category struct {
	ID   int64  `json:"id,omitempty" query:"id" param:"id"`
	Name string `json:"name"`
}

type API struct {
	handlers *handlers.Handlers
}

func New(logic *logic.Logic) *API {
	return &API{
		handlers: handlers.New(logic),
	}
}
