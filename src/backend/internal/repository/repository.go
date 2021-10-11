package repository

import (
	"anekdotas"
	"context"
)

type Repository interface {
	GetQuestionsByTopic(ctx context.Context, topic string) ([]*anekdotas.Question, error)
}
