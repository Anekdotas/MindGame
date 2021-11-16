package db

import (
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

func (r *Repo) GetUserPasswordHash(ctx context.Context, username string) (int64, []byte, error) {
	stmt := fmt.Sprintf("SELECT id, password_hash FROM %s WHERE username = ?", UsersTableName)
	record := &UserRecord{}
	if err := r.db.GetContext(ctx, record, r.db.Rebind(stmt), username); err != nil {
		return 0, nil, errNoRowsToNotFound(err)
	}
	return record.ID, record.PasswordHash, nil
}
