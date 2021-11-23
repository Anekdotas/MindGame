package db

import (
	"anekdotas"
	"anekdotas/internal/repository"
	"database/sql"
	"errors"
	"fmt"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	_ "github.com/lib/pq"
)

var _ repository.Repository = &Repo{}

func NewDB(host string, port int, user, password, dbName string) *sqlx.DB {
	return sqlx.MustConnect(
		"postgres",
		fmt.Sprintf("postgres://%s:%s@%s:%d/%s?sslmode=disable", user, password, host, port, dbName),
	)
}

type Repo struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) *Repo {
	return &Repo{db: db}
}

func (r *Repo) Close() error {
	return r.db.Close()
}

// errNoRowsToNotFound returns anekdotas.ErrNotFound if sql.ErrNoRows was passed. Otherwise, original
// error returned.
func errNoRowsToNotFound(err error) error {
	if errors.Is(err, sql.ErrNoRows) {
		return anekdotas.ErrNotFound
	}
	return err
}

func translateDBError(err error) error {
	pqErr, ok := err.(*pq.Error)
	if !ok {
		return err
	}
	switch pqErr.Code {
	case "23505":
		return anekdotas.ErrAlreadyExists
	}
	return err
}
