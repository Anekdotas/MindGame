package api

import (
	"github.com/labstack/echo/v4"
)

func (a *API) BindApiRoutes(e *echo.Echo, authMiddleware echo.MiddlewareFunc) {
	categories := e.Group("/categories")
	categories.GET("", a.handlers.GetCategories)

	topics := categories.Group("/:categoryId/topics")
	topics.GET("", a.handlers.GetTopics)
	topics.POST("", a.handlers.CreateTopic, authMiddleware)
	topics.GET("/rated", a.handlers.GetRatedTopics, authMiddleware)
	topics.POST("/:topicId/rate", a.handlers.RateTopic, authMiddleware)

	questions := topics.Group("/:topic/questions")
	questions.GET("", a.handlers.GetQuestions, authMiddleware)
	questions.POST("", a.handlers.CreateQuestion, authMiddleware)
	questions.POST("/:questionId/media", a.handlers.UploadMedia, authMiddleware)

	hosts := e.Group("/hosts")
	hosts.GET("/purchased", a.handlers.GetPurchasedHosts, authMiddleware)
	hosts.POST("/purchase", a.handlers.PurchaseHost, authMiddleware)

	users := e.Group("/users")
	users.GET("/stats", a.handlers.GetStats, authMiddleware)
	users.POST("/coins", a.handlers.UpdateCoins, authMiddleware)

	authGroup := users.Group("/auth")
	authGroup.POST("/register", a.handlers.RegisterUser)
	authGroup.POST("/login", a.handlers.Login)

	gameSessions := e.Group("/sessions")
	gameSessions.POST("/finish", a.handlers.FinishGameSession, authMiddleware)
}
