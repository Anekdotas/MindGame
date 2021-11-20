package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"
	"github.com/lib/pq"
)

const QuestionsTableName = "questions"

type QuestionRecord struct {
	ID            int64          `db:"id"`
	TopicID       int            `db:"topic_id"`
	Text          string         `db:"text"`
	MediaURL      sql.NullString `db:"media_url"`
	CorrectAnswer int            `db:"correct_answer"`
	Answers       pq.StringArray `db:"answers"`

	// TopicName used only in CTEs
	TopicName string `db:"topic_name"`
}

func (r *Repo) GetQuestionsByTopic(ctx context.Context, topic string) ([]*anekdotas.Question, error) {
	stmt := fmt.Sprintf(
		"SELECT q.* FROM %s q JOIN %s t ON q.topic_id = t.id WHERE t.name = ?",
		QuestionsTableName, TopicsTableName,
	)
	records := make([]*QuestionRecord, 0)
	if err := r.db.SelectContext(ctx, &records, r.db.Rebind(stmt), topic); err != nil {
		return nil, err
	}
	return deriveFmapQRecordToModel(func(record *QuestionRecord) *anekdotas.Question {
		return &anekdotas.Question{
			ID:            record.ID,
			Text:          record.Text,
			MediaURL:      record.MediaURL.String,
			CorrectAnswer: record.CorrectAnswer,
			Answers:       record.Answers,
		}
	}, records), nil
}

func (r *Repo) CreateQuestion(ctx context.Context, topic string, question *anekdotas.Question) (id int64, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s
		(topic_id, text, correct_answer, answers)
		VALUES (
			(SELECT id FROM %s WHERE name=:topic_name),
			:text,
			:correct_answer,
			:answers
		) RETURNING id`,
		QuestionsTableName,
		TopicsTableName,
	)
	record := &QuestionRecord{
		Text:          question.Text,
		CorrectAnswer: question.CorrectAnswer,
		Answers:       question.Answers,
		TopicName:     topic,
	}
	query, args, err := r.db.BindNamed(stmt, record)
	if err != nil {
		return
	}
	err = r.db.GetContext(ctx, &id, query, args...)
	return
}

func (r *Repo) UpdateMediaURL(ctx context.Context, questionID int64, mediaURL string) error {
	stmt := fmt.Sprintf("UPDATE %s SET media_url = ? WHERE id = ?", QuestionsTableName)
	_, err := r.db.ExecContext(ctx, r.db.Rebind(stmt), mediaURL, questionID)
	return err
}
