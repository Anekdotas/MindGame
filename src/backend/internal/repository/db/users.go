package db

import (
	"anekdotas"
	"context"
	"database/sql"
	"fmt"
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
		return 0, nil, errNoRowsToNotFound(err)
	}
	return record.ID, record.PasswordHash, nil
}
