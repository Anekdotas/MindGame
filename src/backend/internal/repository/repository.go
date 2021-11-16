package repository

import (
	"anekdotas"
	"context"
)

type Repository interface {
	GetQuestionsByTopic(ctx context.Context, topic string) ([]*anekdotas.Question, error)
	CreateQuestion(ctx context.Context, topic string, question *anekdotas.Question) (id int64, err error)
	UpdateMediaURL(ctx context.Context, questionID int64, mediaURL string) error
	GetTopicsByCategoryID(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error)
	CreateTopic(ctx context.Context, categoryID int64, topic *anekdotas.Topic) (name string, err error)
	GetCategories(ctx context.Context) ([]*anekdotas.Category, error)
	GetUserPasswordHash(ctx context.Context, username string) (int64, []byte, error)
}
