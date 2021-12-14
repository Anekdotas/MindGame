package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"

	"github.com/jmoiron/sqlx"
	"github.com/pkg/errors"
)

const QuestionsTableName = "questions"

type QuestionRecord struct {
	ID       int64          `db:"id"`
	TopicID  int            `db:"topic_id"`
	Text     string         `db:"text"`
	MediaURL sql.NullString `db:"media_url"`
	// CorreectAnswersID stores ID of the correct answer to this question. Despite of being nullable, we are not
	// querying any questions with NULL-valued "correct_answer" so the value is always present in Go struct.
	CorrectAnswerID int64 `db:"correct_answer"`

	// TopicName used only in CTEs
	TopicName string `db:"topic_name"`
}

func (r *Repo) GetQuestionsByTopic(ctx context.Context, topic string, userID int64) (int64, []*anekdotas.Question, error) {
	tx, err := r.db.BeginTxx(ctx, &sql.TxOptions{})
	if err != nil {
		return 0, nil, err
	}
	defer tx.Rollback() // if transaction is committed, Rollback is no-op
	records, err := r.getQuestions(tx, topic)
	if err != nil {
		return 0, nil, err
	}
	preparedStmt, err := r.prepareGetAnswersQuery(tx)
	if err != nil {
		return 0, nil, err
	}
	defer func() {
		preparedStmt.Close()
	}()
	questions := make([]*anekdotas.Question, 0, len(records))
	ids := make([]int64, 0, len(records))
	for _, record := range records {
		answers, err := r.getAnswersWithPreparedStmt(ctx, preparedStmt, record.ID)
		if err != nil {
			return 0, nil, err
		}
		questions = append(questions, &anekdotas.Question{
			ID:              record.ID,
			Text:            record.Text,
			MediaURL:        record.MediaURL.String,
			CorrectAnswerID: record.CorrectAnswerID,
			Answers:         answers,
		})
		ids = append(ids, record.ID)
	}
	gameSessionID, err := r.createGameSession(tx, userID)
	if err != nil {
		return 0, nil, err
	}
	if err = r.setQuestionsForGameSession(tx, gameSessionID, ids); err != nil {
		return 0, nil, err
	}
	return gameSessionID, questions, tx.Commit()
}

func (r *Repo) CreateQuestionWithAnswers(ctx context.Context, topic string, question *anekdotas.Question, answers []*anekdotas.Answer) (id int64, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s
		(topic_id, text)
		VALUES (
			(SELECT id FROM %s WHERE name=:topic_name),
			:text
		) RETURNING id`,
		QuestionsTableName,
		TopicsTableName,
	)
	tx, err := r.db.BeginTxx(ctx, &sql.TxOptions{})
	if err != nil {
		return 0, err
	}
	defer tx.Rollback()
	record := &QuestionRecord{
		Text:      question.Text,
		TopicName: topic,
	}
	query, args, err := tx.BindNamed(stmt, record)
	if err != nil {
		return
	}
	if err = tx.Get(&id, query, args...); err != nil {
		return 0, errors.Wrap(translateDBError(err), "failed to create a new question")
	}
	if err = r.setAnswersForQuestion(tx, id, answers); err != nil {
		return 0, errors.Wrap(err, "failed to set answers for new question")
	}
	if err = r.setCorrectAnswer(tx, id, answers[question.CorrectAnswerID-1].Text); err != nil {
		return 0, errors.Wrap(err, "failed to set correct answer for new question")
	}
	return id, tx.Commit()
}

func (r *Repo) UpdateMediaURL(ctx context.Context, questionID int64, mediaURL string) error {
	stmt := fmt.Sprintf("UPDATE %s SET media_url = ? WHERE id = ?", QuestionsTableName)
	_, err := r.db.ExecContext(ctx, r.db.Rebind(stmt), mediaURL, questionID)
	return err
}

func (r *Repo) getQuestions(tx *sqlx.Tx, topic string) ([]*QuestionRecord, error) {
	stmt := fmt.Sprintf(
		`SELECT q.* FROM %s q
		JOIN %s t ON q.topic_id = t.id
		WHERE t.name = ? AND q.correct_answer IS NOT NULL`,
		QuestionsTableName, TopicsTableName,
	)
	records := make([]*QuestionRecord, 0)
	if err := tx.Select(&records, r.db.Rebind(stmt), topic); err != nil {
		return nil, translateDBError(err)
	}
	return records, nil
}

func (r *Repo) setCorrectAnswer(tx *sqlx.Tx, questionID int64, answerText string) error {
	stmt := fmt.Sprintf(
		"UPDATE %s SET correct_answer = (SELECT id FROM %s WHERE text = ?) WHERE id = ?",
		QuestionsTableName, AnswersTableName,
	)
	_, err := tx.Exec(tx.Rebind(stmt), answerText, questionID)
	return translateDBError(err)
}
