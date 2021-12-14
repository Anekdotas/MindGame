package db

import (
	"anekdotas"
	"context"
	"fmt"
	"math"
	"time"

	"github.com/jmoiron/sqlx"
)

const (
	GameSessionsTableName     = "game_sessions"
	SessionQuestionsTableName = "questions_sessions"
	ChosenAnswersTableName    = "chosen_answers"
)

func (r *Repo) createGameSession(tx *sqlx.Tx, userID int64) (id int64, err error) {
	stmt := fmt.Sprintf("INSERT INTO %s (user_id) VALUES (?) RETURNING id", GameSessionsTableName)
	err = tx.Get(&id, tx.Rebind(stmt), userID)
	return
}

func (r *Repo) setQuestionsForGameSession(tx *sqlx.Tx, gameSessionID int64, questionIDs []int64) error {
	stmt := fmt.Sprintf(
		"INSERT INTO %s (game_session_id, question_id) VALUES %s",
		SessionQuestionsTableName,
		generateBindvars(len(questionIDs), 2),
	)
	args := make([]interface{}, 0, len(questionIDs)*2)
	for _, qID := range questionIDs {
		args = append(args, gameSessionID, qID)
	}
	_, err := tx.Exec(tx.Rebind(stmt), args...)
	return translateDBError(err)
}

func (r *Repo) UpdateStatistics(ctx context.Context, userID int64, statistics *anekdotas.Statistics) error {
	if err := r.verifyGameSessionOwner(ctx, userID, statistics.GameSessionID); err != nil {
		return err
	}
	if finished, err := r.isGameSessionFinished(ctx, statistics.GameSessionID); err != nil {
		return err
	} else if finished {
		return anekdotas.ErrGameSessionAlreadyFinished
	}
	if err := r.SetChosenAnswers(ctx, userID, statistics.GameSessionID, statistics.Choices); err != nil {
		return err
	}
	return r.updateTimeSpentAndStreak(ctx, statistics.GameSessionID, statistics.TimeSpent, statistics.Streak)
}

func (r *Repo) SetChosenAnswers(ctx context.Context, userID, gameSessionID int64, choices []*anekdotas.Choice) error {
	stmt := fmt.Sprintf(
		"INSERT INTO %s (game_session_id, question_id, answer_id) VALUES %s",
		ChosenAnswersTableName,
		generateBindvars(len(choices), 3),
	)
	args := make([]interface{}, 0, len(choices)*3)
	for _, choice := range choices {
		args = append(args, gameSessionID, choice.QuestionID, choice.AnswerID)
	}
	_, err := r.db.ExecContext(ctx, r.db.Rebind(stmt), args...)
	return translateDBError(err)
}

func (r *Repo) updateTimeSpentAndStreak(ctx context.Context, gameSessionID int64, timeSpent time.Duration, streak uint) error {
	stmt := fmt.Sprintf("UPDATE %s SET time_spent = ?, streak = ? WHERE id = ?", GameSessionsTableName)
	_, err := r.db.ExecContext(ctx, r.db.Rebind(stmt), math.Round(timeSpent.Seconds()), streak, gameSessionID)
	return translateDBError(err)
}

// verifyGameSessionOwner tries to get a Game Session with specified ID and specified User ID. If a Game Session with specified
// ID has different User ID, this function will return anekdotas.ErrNotFound.
func (r *Repo) verifyGameSessionOwner(ctx context.Context, userID, gameSessionId int64) error {
	stmt := fmt.Sprintf("SELECT id FROM %s WHERE id=? AND user_id=?", GameSessionsTableName)
	err := r.db.GetContext(ctx, &gameSessionId, r.db.Rebind(stmt), gameSessionId, userID)
	return translateDBError(err)
}

func (r *Repo) isGameSessionFinished(ctx context.Context, gameSessionID int64) (bool, error) {
	stmt := fmt.Sprintf("SELECT count(game_session_id) FROM %s WHERE game_session_id=?", ChosenAnswersTableName)
	var counter int
	err := r.db.GetContext(ctx, &counter, r.db.Rebind(stmt), gameSessionID)
	return counter > 0, translateDBError(err)
}
