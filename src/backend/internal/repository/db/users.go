package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"
	"time"
)

const UsersTableName = "users"

type UserRecord struct {
	ID           int64          `db:"id"`
	Username     string         `db:"username"`
	Email        sql.NullString `db:"email"`
	PasswordHash []byte         `db:"password_hash"`
}

func (r *Repo) CreateUser(ctx context.Context, user *anekdotas.User, passwordHash []byte) (id int64, err error) {
	stmt := fmt.Sprintf(
		`INSERT INTO %s (username, email, password_hash)
		VALUES (:username, :email, :password_hash)
		RETURNING id`,
		UsersTableName,
	)
	var email sql.NullString
	if err = email.Scan(user.Email); err != nil {
		return
	}
	record := &UserRecord{
		Username:     user.Username,
		Email:        email,
		PasswordHash: passwordHash,
	}
	query, args, err := r.db.BindNamed(stmt, record)
	if err != nil {
		return
	}
	err = r.db.GetContext(ctx, &id, query, args...)
	return id, translateDBError(err)
}

func (r *Repo) GetUserPasswordHash(ctx context.Context, username string) (int64, []byte, error) {
	stmt := fmt.Sprintf("SELECT id, password_hash FROM %s WHERE username = ?", UsersTableName)
	record := &UserRecord{}
	if err := r.db.GetContext(ctx, record, r.db.Rebind(stmt), username); err != nil {
		return 0, nil, translateDBError(err)
	}
	return record.ID, record.PasswordHash, nil
}

func (r *Repo) UpdateUserCoins(ctx context.Context, userID int64, coinsDelta int) error {
	stmt := fmt.Sprintf("UPDATE %s SET coins = coins + ? WHERE id = ?", UsersTableName)
	_, err := r.db.ExecContext(ctx, r.db.Rebind(stmt), coinsDelta, userID)
	return translateDBError(err)
}

func (r *Repo) GetUserStats(ctx context.Context, userID int64) (*anekdotas.UserStatistics, error) {
	stats := new(anekdotas.UserStatistics)
	var err error
	stats.Coins, err = r.getUserCoins(ctx, userID)
	if err != nil {
		return nil, err
	}
	stats.CorrectAnswers, stats.CorrectAnswersPercentage, err = r.getCorrectAnswers(ctx, userID)
	if err != nil {
		return nil, err
	}
	stats.LongestStreak, stats.LongestStreakTopic, err = r.getLongestStreak(ctx, userID)
	if err != nil {
		return nil, err
	}
	stats.TotalTimeSpent, stats.AverageGameTime, err = r.getGameTimeStats(ctx, userID)
	if err != nil {
		return nil, err
	}
	stats.TopicsCreated, stats.TopicsRated, stats.TopicsPlayed, err = r.getTopicsStats(ctx, userID)
	return stats, err
}

func (r *Repo) getUserCoins(ctx context.Context, userID int64) (coins uint, err error) {
	stmt := fmt.Sprintf("SELECT coins FROM %s WHERE id = ?", UsersTableName)
	return coins, translateDBError(r.db.GetContext(ctx, &coins, r.db.Rebind(stmt), userID))
}

func (r *Repo) getCorrectAnswers(ctx context.Context, userID int64) (uint16, float32, error) {
	stmt := fmt.Sprintf(
		`SELECT sum(is_correct::int) AS correct_answers, ROUND(AVG(is_correct::int) * 100, 2) AS correct_answers_percentage FROM
		(
			SELECT COALESCE(ca.answer_id=q.correct_answer, 'f') AS is_correct
			FROM %s AS gs
			JOIN %s AS qs ON qs.game_session_id = gs.id
			LEFT JOIN %s AS ca ON ca.game_session_id = gs.id AND ca.question_id = qs.question_id
			JOIN %s AS q ON q.id = qs.question_id
			WHERE gs.user_id = ?
		) AS sq`,
		GameSessionsTableName, SessionQuestionsTableName, ChosenAnswersTableName, QuestionsTableName,
	)
	record := new(struct {
		CorrectAnswers           uint16  `db:"correct_answers"`
		CorrectAnswersPercentage float32 `db:"correct_answers_percentage"`
	})
	return record.CorrectAnswers, record.CorrectAnswersPercentage, translateDBError(r.db.GetContext(ctx, record, r.db.Rebind(stmt), userID))
}

func (r *Repo) getLongestStreak(ctx context.Context, userID int64) (longestStreak uint16, topicName string, err error) {
	stmt := fmt.Sprintf("SELECT max(streak) FROM %s WHERE user_id = ?", GameSessionsTableName)
	if err := r.db.GetContext(ctx, &longestStreak, r.db.Rebind(stmt), userID); err != nil {
		return 0, "", translateDBError(err)
	}
	stmt = fmt.Sprintf(
		`SELECT name FROM %s AS t
		JOIN %s AS q ON q.topic_id = t.id
		JOIN %s AS qs ON qs.question_id = q.id
		JOIN %s AS gs ON gs.id = qs.game_session_id
		JOIN (
			SELECT user_id, max(streak) AS streak FROM %s GROUP BY user_id
		) AS temp_gs ON temp_gs.user_id = gs.user_id
		WHERE gs.user_id = ? AND gs.streak = temp_gs.streak
		LIMIT 1`,
		TopicsTableName, QuestionsTableName, SessionQuestionsTableName, GameSessionsTableName, GameSessionsTableName,
	)
	return longestStreak, topicName, translateDBError(r.db.GetContext(ctx, &topicName, r.db.Rebind(stmt), userID))
}

func (r *Repo) getGameTimeStats(ctx context.Context, userID int64) (time.Duration, time.Duration, error) {
	stmt := fmt.Sprintf("SELECT SUM(time_spent) as total, AVG(time_spent) as average FROM %s WHERE user_id = ? AND time_spent > 0", GameSessionsTableName)
	ret := new(struct {
		TotalTime   uint16  `db:"total"`
		AverageTime float32 `db:"average"`
	})
	if err := r.db.GetContext(ctx, ret, r.db.Rebind(stmt), userID); err != nil {
		return 0, 0, translateDBError(err)
	}
	return time.Second * time.Duration(ret.TotalTime), time.Second * time.Duration(ret.AverageTime), nil
}

func (r *Repo) getTopicsStats(ctx context.Context, userID int64) (topicsCreated uint16, topicsRated uint16, topicsPlayed uint16, err error) {
	stmt := fmt.Sprintf("SELECT count(id) FROM %s WHERE author_id = ?", TopicsTableName)
	if err = r.db.GetContext(ctx, &topicsCreated, r.db.Rebind(stmt), userID); err != nil {
		err = translateDBError(err)
		return
	}
	stmt = fmt.Sprintf(
		`SELECT count(topic_id) FROM %s AS r
		JOIN %s AS u on u.id = r.user_id
		WHERE u.id = ?`,
		RatesTableName, UsersTableName,
	)
	if err = r.db.GetContext(ctx, &topicsRated, r.db.Rebind(stmt), userID); err != nil {
		err = translateDBError(err)
		return
	}
	stmt = fmt.Sprintf(
		`SELECT COUNT(distinct q.topic_id) FROM %s AS q
		JOIN %s AS qs ON qs.question_id = q.id
		JOIN %s AS gs ON gs.id = qs.game_session_id
		WHERE gs.user_id = ?`,
		QuestionsTableName, SessionQuestionsTableName, GameSessionsTableName,
	)
	return topicsCreated, topicsRated, topicsPlayed, r.db.GetContext(ctx, &topicsPlayed, r.db.Rebind(stmt), userID)
}
