package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"
)

type TopicRecord struct {
	ID               int64          `db:"id"`
	Name             string         `db:"name"`
	Description      string         `db:"description"`
	CategoryID       int64          `db:"category_id"`
	AuthorID         int64          `db:"author_id"`
	QuestionsPerGame int            `db:"questions_per_game"`
	ImageURL         sql.NullString `db:"image_url"`
	Difficulty       int            `db:"difficulty"`

	// Rating is aggregated read-only value
	Rating sql.NullFloat64 `db:"rating"`
	// AuthorName only used in SELECT statements
	AuthorName string `db:"author_name"`
}

type RateRecord struct {
	TopicID int64   `db:"topic_id"`
	UserID  int64   `db:"user_id"`
	Rating  float32 `db:"rating"`
}

const (
	TopicsTableName = "topics"
	RatesTableName  = "rates"
)

func (r *Repo) GetTopicsByCategoryID(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error) {
	stmt := fmt.Sprintf(
		`SELECT t.id, name, avg(r.rating) AS rating, description, u.username AS author_name, image_url, difficulty
		FROM %s t
		JOIN %s u ON u.id = t.author_id
		LEFT JOIN %s r ON r.topic_id = t.id
		WHERE category_id = ?
		GROUP BY t.id, u.username`,
		TopicsTableName, UsersTableName, RatesTableName,
	)
	records := make([]*TopicRecord, 0)
	if err := r.db.SelectContext(ctx, &records, r.db.Rebind(stmt), categoryID); err != nil {
		return nil, translateDBError(err)
	}
	return deriveFmapTRecordToModel(func(record *TopicRecord) *anekdotas.Topic {
		return &anekdotas.Topic{
			ID:          record.ID,
			Name:        record.Name,
			Description: record.Description,
			Author:      record.AuthorName,
			Rating:      float32(record.Rating.Float64),
			ImageURL:    record.ImageURL.String,
			Difficulty:  record.Difficulty,
		}
	}, records), nil
}

func (r *Repo) GetRatedTopicsByUserID(ctx context.Context, userID int64) ([]*anekdotas.Topic, error) {
	stmt := fmt.Sprintf(
		"SELECT t.id FROM %s AS t JOIN %s AS r ON r.topic_id = t.id WHERE r.user_id = ?",
		TopicsTableName, RatesTableName,
	)
	records := make([]*TopicRecord, 0)
	if err := r.db.SelectContext(ctx, &records, r.db.Rebind(stmt), userID); err != nil {
		return nil, translateDBError(err)
	}
	return deriveFmapTRecordToModel(func(tr *TopicRecord) *anekdotas.Topic {
		return &anekdotas.Topic{ID: tr.ID}
	}, records), nil
}

func (r *Repo) CreateTopic(ctx context.Context, categoryID int64, authorID int64, topic *anekdotas.Topic) (name string, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s
		(name, author_id, description, category_id, questions_per_game, image_url, difficulty)
		VALUES (
			:name,
			:author_id,
			:description,
			:category_id,
			:questions_per_game,
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
		AuthorID:    authorID,
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

func (r *Repo) RateTopicByID(ctx context.Context, userID, topicID int64, rating float32) error {
	stmt := fmt.Sprintf(
		"INSERT INTO %s (topic_id, user_id, rating) VALUES (?, ?, ?)", RatesTableName,
	)
	_, err := r.db.ExecContext(ctx, r.db.Rebind(stmt), topicID, userID, rating)
	return translateDBError(err)
}
