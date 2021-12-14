package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"

	"github.com/jmoiron/sqlx"
	"github.com/pkg/errors"
)

const (
	AnswersTableName          = "answers"
	QuestionsAnswersTableName = "questions_answers"
)

type AnswerRecord struct {
	ID   int64  `json:"id"`
	Text string `json:"text"`
}

func (r *Repo) prepareGetAnswersQuery(tx *sqlx.Tx) (*sqlx.Stmt, error) {
	stmt := fmt.Sprintf(
		`SELECT id, text FROM %s a JOIN %s qa ON qa.answer_id = a.id WHERE qa.question_id = ?`,
		AnswersTableName, QuestionsAnswersTableName,
	)
	return tx.Preparex(r.db.Rebind(stmt))
}

func (r *Repo) getAnswersWithPreparedStmt(ctx context.Context, stmt *sqlx.Stmt, questionID int64) ([]*anekdotas.Answer, error) {
	records := make([]*AnswerRecord, 0)
	if err := stmt.SelectContext(ctx, &records, questionID); err != nil {
		return nil, err
	}
	return deriveFmapARecordToModel(func(record *AnswerRecord) *anekdotas.Answer {
		return &anekdotas.Answer{
			ID:   record.ID,
			Text: record.Text,
		}
	}, records), nil
}

func (r *Repo) getExistingAnswersIDs(tx *sqlx.Tx, answers []*AnswerRecord) ([]int64, error) {
	stmt := fmt.Sprintf(
		"SELECT id FROM %s WHERE text IN %s",
		AnswersTableName,
		generateBindvars(1, len(answers)),
	)
	ids := make([]int64, 0)
	args := deriveFmapAnswersTextToInterface(
		func(a *AnswerRecord) interface{} {
			return a.Text
		}, answers,
	)
	if err := tx.Select(&ids, tx.Rebind(stmt), args...); err != nil && !errors.Is(err, sql.ErrNoRows) {
		return nil, translateDBError(err)
	}
	return ids, nil
}

func (r *Repo) bulkCreateAnswers(tx *sqlx.Tx, answers []*AnswerRecord) ([]int64, error) {
	stmt := fmt.Sprintf("INSERT INTO %s (text) VALUES (:text) ON CONFLICT DO NOTHING RETURNING id", AnswersTableName)
	rows, err := tx.NamedQuery(tx.Rebind(stmt), answers)
	if err != nil {
		return nil, translateDBError(err)
	}
	ids := make([]int64, 0)
	for rows.Next() {
		var id int64
		rows.Scan(&id)
		ids = append(ids, id)
	}
	return ids, nil
}

func (r *Repo) linkAnswersToQuestion(tx *sqlx.Tx, questionID int64, answersIDs []int64) error {
	stmt := fmt.Sprintf(
		"INSERT INTO %s (question_id, answer_id) VALUES %s",
		QuestionsAnswersTableName,
		generateBindvars(len(answersIDs), 2),
	)
	args := make([]interface{}, 0, len(answersIDs)*2)
	for _, aID := range answersIDs {
		args = append(args, questionID, aID)
	}
	_, err := tx.Exec(tx.Rebind(stmt), args...)
	return translateDBError(err)
}

func (r *Repo) setAnswersForQuestion(tx *sqlx.Tx, questionID int64, modelAnswers []*anekdotas.Answer) error {
	answers := deriveFmapAModelToRecord(func(answer *anekdotas.Answer) *AnswerRecord {
		return &AnswerRecord{
			Text: answer.Text,
		}
	}, modelAnswers)
	existingAnswersIDs, err := r.getExistingAnswersIDs(tx, answers)
	if err != nil {
		return errors.Wrap(err, "failed to get existing answers IDs")
	}
	answersIDs, err := r.bulkCreateAnswers(tx, answers)
	if err != nil {
		return errors.Wrap(err, "failed to create new answers")
	}
	if err := r.linkAnswersToQuestion(tx, questionID, append(existingAnswersIDs, answersIDs...)); err != nil {
		return errors.Wrap(err, "failed to link answers to question")
	}
	return nil
}
