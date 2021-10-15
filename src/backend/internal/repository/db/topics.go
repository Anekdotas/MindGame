package db

import (
	"anekdotas"
	"context"
	"fmt"
)

type TopicRecord struct {
	ID               int    `db:"id"`
	Name             string `db:"name"`
	Author           string `db:"author"`
	QuestionsPerGame int    `db:"question_per_game"`
}

const TopicsTableName = "topics"

func (r *Repo) GetTopics(ctx context.Context) ([]*anekdotas.Topic, error) {
	stmt := fmt.Sprintf("SELECT id, name FROM %s", TopicsTableName)
	records := make([]*TopicRecord, 0)
	if err := r.db.SelectContext(ctx, &records, stmt); err != nil {
		return nil, err
	}
	return deriveFmapTRecordToModel(func(record *TopicRecord) *anekdotas.Topic {
		return &anekdotas.Topic{
			ID:     record.ID,
			Name:   record.Name,
		}
	}, records), nil
}
