package api

import "github.com/labstack/echo/v4"

func (a *API) BindApiRoutes(e *echo.Echo) {
	categories := e.Group("/categories")
	categories.GET("", a.handlers.GetCategories)

	topics := categories.Group("/:categoryId/topics")
	topics.GET("", a.handlers.GetTopics)
	topics.POST("", a.handlers.CreateTopic)

	questions := topics.Group("/:topic/questions")
	questions.GET("", a.handlers.GetQuestions)
	questions.POST("", a.handlers.CreateQuestion)
	questions.POST("/:questionId/media", a.handlers.UploadMedia)

	authGroup := e.Group("/auth")
	authGroup.POST("/register", a.handlers.RegisterUser)
	authGroup.POST("/login", a.handlers.Login)
}
