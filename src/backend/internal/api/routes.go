package api

import "github.com/labstack/echo/v4"

func (a *API) BindQuestionsRoutes(e *echo.Echo) {
	e.GET("/topics", a.handlers.GetTopics)
	e.GET("topics/:topic/questions", a.handlers.GetQuestions)
}
