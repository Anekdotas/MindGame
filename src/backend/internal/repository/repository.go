package repository

import (
	"anekdotas"
	"context"
)

type Repository interface {
	GetQuestionsByTopic(ctx context.Context, topic string) ([]*anekdotas.Question, error)
	CreateQuestion(ctx context.Context, topic string, question *anekdotas.Question) (id int64, err error)
	UpdateMediaURL(ctx context.Context, questionID int64, mediaURL string) error
	GetTopics(ctx context.Context) ([]*anekdotas.Topic, error)
	CreateTopic(ctx context.Context, topic *anekdotas.Topic) (name string, err error)
	GetCategories(ctx context.Context) ([]*anekdotas.Category, error)
}
