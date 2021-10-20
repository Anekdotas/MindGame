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
	QuestionsPerGame int    `db:"questions_per_game"`
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

func (r *Repo) CreateTopic(ctx context.Context, topic *anekdotas.Topic) (name string, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s
		(name, author, questions_per_game)
		VALUES (
			:name,
			:author,
			:questions_per_game
		) RETURNING name`,
		TopicsTableName,
	)
	record := &TopicRecord{
		Name:             topic.Name,
		Author:           topic.Author,
		// TODO: AN-36 - use actual questions_per_game
		QuestionsPerGame: 10,
	}
	if _, err = r.db.NamedExecContext(ctx, stmt, record); err != nil {
		return
	}
	return topic.Name, nil
}
