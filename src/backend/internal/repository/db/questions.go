package db

import (
	"anekdotas"
	"context"
	"fmt"
	"github.com/lib/pq"
)

const QuestionsTableName = "questions"

type QuestionRecord struct {
	ID            int            `db:"id"`
	TopicID       int            `db:"topic_id"`
	Text          string         `db:"text"`
	MediaURL      string         `db:"media_url"`
	CorrectAnswer int            `db:"correct_answer"`
	Answers       pq.StringArray `db:"answers"`
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
			MediaURL:      record.MediaURL,
			CorrectAnswer: record.CorrectAnswer,
			Answers:       record.Answers,
		}
	}, records), nil
}
