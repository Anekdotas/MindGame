package api

import "github.com/labstack/echo/v4"

func (a *API) BindApiRoutes(e *echo.Echo) {
	e.GET("/categories", a.handlers.GetCategories)
	e.GET("/categories/:category/topics", a.handlers.GetTopics)
	e.POST("/categories/:category/topics", a.handlers.CreateTopic)
	e.GET("/categories/:category/topics/:topic/questions", a.handlers.GetQuestions)
	e.POST("/categories/:category/topics/:topic/questions", a.handlers.CreateQuestion)
	e.POST("/categories/:category/topics/:topic/questions/:id/media", a.handlers.UploadMedia)
}
