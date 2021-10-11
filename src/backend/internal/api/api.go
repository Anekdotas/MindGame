package api

import "anekdotas/internal/logic"

type Question struct {
	ID               int      `json:"id,omitempty" query:"id" param:"id"`
	Text             string   `json:"text"`
	MediaURL         string   `json:"mediaUrl,omitempty"`
	CorrectAnswer    string   `json:"correctAnswer"`
	IncorrectAnswers []string `json:"incorrectAnswers"`
}

type API struct {
	handlers *handlers
}

func New(logic *logic.Logic) *API {
	return &API{
		handlers: newHandlers(logic),
	}
}
