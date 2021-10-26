package api

import "github.com/labstack/echo/v4"

func (a *API) BindApiRoutes(e *echo.Echo) {
	e.GET("/topics", a.handlers.GetTopics)
	e.POST("/topics", a.handlers.CreateTopic)
	e.GET("/topics/:topic/questions", a.handlers.GetQuestions)
	e.POST("/topics/:topic/questions", a.handlers.CreateQuestion)
	e.POST("/topics/:topic/questions/:id/media", a.handlers.UploadMedia)
}
