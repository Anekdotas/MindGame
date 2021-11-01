package db

import (
	"anekdotas"
	"context"
	"fmt"
)

type TopicRecord struct {
	ID               int64  `db:"id"`
	Name             string `db:"name"`
	CategoryID       int64  `db:"category_id"`
	Author           string `db:"author"`
	QuestionsPerGame int    `db:"questions_per_game"`
}

const TopicsTableName = "topics"

func (r *Repo) GetTopicsByCategoryID(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error) {
	stmt := fmt.Sprintf("SELECT id, name, author FROM %s WHERE category_id = ?", TopicsTableName)
	records := make([]*TopicRecord, 0)
	if err := r.db.SelectContext(ctx, &records, r.db.Rebind(stmt), categoryID); err != nil {
		return nil, err
	}
	return deriveFmapTRecordToModel(func(record *TopicRecord) *anekdotas.Topic {
		return &anekdotas.Topic{
			ID:     record.ID,
			Name:   record.Name,
			Author: record.Author,
		}
	}, records), nil
}

func (r *Repo) CreateTopic(ctx context.Context, categoryID int64, topic *anekdotas.Topic) (name string, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s
		(name, author, category_id, questions_per_game)
		VALUES (
			:name,
			:author,
			:category_id,
			:questions_per_game
		) RETURNING name`,
		TopicsTableName,
	)
	record := &TopicRecord{
		Name:       topic.Name,
		Author:     topic.Author,
		CategoryID: categoryID,
		// TODO: AN-36 - use actual questions_per_game
		QuestionsPerGame: 10,
	}
	if _, err = r.db.NamedExecContext(ctx, stmt, record); err != nil {
		return
	}
	return topic.Name, nil
}
