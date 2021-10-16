package api

import "anekdotas/internal/logic"

type Question struct {
	ID            int      `json:"id,omitempty" query:"id" param:"id"`
	Text          string   `json:"text"`
	MediaURL      string   `json:"mediaUrl,omitempty"`
	CorrectAnswer int      `json:"correctAnswer"`
	Answers       []string `json:"answers"`
}

type Topic struct {
	ID     int    `json:"id,omitempty" query:"id" param:"id"`
	Name   string `json:"name"`
	Author string `json:"author"`
}

type API struct {
	handlers *handlers
}

func New(logic *logic.Logic) *API {
	return &API{
		handlers: newHandlers(logic),
	}
}
