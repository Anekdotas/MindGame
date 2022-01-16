package repository

import (
	"anekdotas"
	"context"
)

type Repository interface {
	GetQuestionsByTopic(ctx context.Context, topic string, userID int64) (int64, []*anekdotas.Question, error)
	CreateQuestionWithAnswers(ctx context.Context, topic string, question *anekdotas.Question, answers []*anekdotas.Answer) (id int64, err error)
	UpdateMediaURL(ctx context.Context, questionID int64, mediaURL string) error
	GetTopicsByCategoryID(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error)
	GetRatedTopicsByUserID(ctx context.Context, userID int64) ([]*anekdotas.Topic, error)
	CreateTopic(ctx context.Context, categoryID int64, authorID int64, topic *anekdotas.Topic) (name string, err error)
	RateTopicByID(ctx context.Context, userID, topicID int64, rating float32) error
	GetCategories(ctx context.Context) ([]*anekdotas.Category, error)
	CreateUser(ctx context.Context, user *anekdotas.User, passwordHash []byte) (id int64, err error)
	GetUserPasswordHash(ctx context.Context, username string) (int64, []byte, error)
	UpdateStatistics(ctx context.Context, userID int64, statistics *anekdotas.GameAnalytics) error
	UpdateUserCoins(ctx context.Context, userID int64, coinsDelta int) error
	GetUserStats(ctx context.Context, userID int64) (*anekdotas.UserStatistics, error)
}
