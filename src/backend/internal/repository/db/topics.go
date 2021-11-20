package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"
	"math/rand"
	"time"
)

type TopicRecord struct {
	ID               int64          `db:"id"`
	Name             string         `db:"name"`
	Description      string         `db:"description"`
	CategoryID       int64          `db:"category_id"`
	Author           string         `db:"author"`
	QuestionsPerGame int            `db:"questions_per_game"`
	ImageURL         sql.NullString `db:"image_url"`
	Difficulty       int            `db:"difficulty"`
}

const TopicsTableName = "topics"

func (r *Repo) GetTopicsByCategoryID(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error) {
	stmt := fmt.Sprintf("SELECT id, name, description, author, image_url, difficulty FROM %s WHERE category_id = ?", TopicsTableName)
	records := make([]*TopicRecord, 0)
	if err := r.db.SelectContext(ctx, &records, r.db.Rebind(stmt), categoryID); err != nil {
		return nil, err
	}
	rand.Seed(time.Now().Unix())
	return deriveFmapTRecordToModel(func(record *TopicRecord) *anekdotas.Topic {
		return &anekdotas.Topic{
			ID:          record.ID,
			Name:        record.Name,
			Description: record.Description,
			Author:      record.Author,
			Rating:      rand.Float32() * 5,
			ImageURL:    record.ImageURL.String,
			Difficulty:  record.Difficulty,
		}
	}, records), nil
}

func (r *Repo) CreateTopic(ctx context.Context, categoryID int64, topic *anekdotas.Topic) (name string, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s
		(name, author, description, category_id, questions_per_game, image_url, difficulty)
		VALUES (
			:name,
			:author,
			:description,
			:category_id,
			:questions_per_game
			:image_url,
			:difficulty
		) RETURNING name`,
		TopicsTableName,
	)
	var imageURL sql.NullString
	if err = imageURL.Scan(topic.ImageURL); err != nil {
		return
	}
	record := &TopicRecord{
		Name:        topic.Name,
		Author:      topic.Author,
		Description: topic.Description,
		CategoryID:  categoryID,
		// TODO: AN-36 - use actual questions_per_game
		QuestionsPerGame: 10,
		ImageURL:         imageURL,
		// TODO: check for default value
		Difficulty: topic.Difficulty,
	}
	if _, err = r.db.NamedExecContext(ctx, stmt, record); err != nil {
		return
	}
	return topic.Name, nil
}
