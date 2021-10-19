package logic

import (
	"anekdotas"
	"anekdotas/internal/repository"
	"context"
)

type Logic struct {
	repo repository.Repository
}

func New(repo repository.Repository) *Logic {
	return &Logic{repo: repo}
}

func (l *Logic) GetQuestionsByTopic(ctx context.Context, topic string) ([]*anekdotas.Question, error) {
	return l.repo.GetQuestionsByTopic(ctx, topic)
}

func (l *Logic) CreateQuestion(ctx context.Context, topic string, question anekdotas.Question) (id int64, err error) {
	return l.repo.CreateQuestion(ctx, topic, question)
}

func (l *Logic) GetAllTopics(ctx context.Context) ([]*anekdotas.Topic, error) {
	return l.repo.GetTopics(ctx)
}
