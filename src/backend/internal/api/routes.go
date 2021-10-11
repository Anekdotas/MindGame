package api

import "github.com/labstack/echo/v4"

func (a *API) BindQuestionsRoutes(e *echo.Echo) {
	e.GET("/questions", a.handlers.GetQuestions)
}
