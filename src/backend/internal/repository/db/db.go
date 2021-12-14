package db

import (
	"anekdotas"
	"anekdotas/internal/repository"
	"database/sql"
	"fmt"
	"strings"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"github.com/pkg/errors"
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

// translateDBError returns corresponding anekdotas error to the passed DB error.
//
// Current translations:
//  sql.ErrNoRows -> anekdotas.ErrNotFound
//  PostgreSQL Error Code 23505 -> anekdotas.ErrAlreadyExists
//  Unknown error -> original error
func translateDBError(err error) error {
	if err == nil {
		return nil
	}
	if errors.Is(err, sql.ErrNoRows) {
		return anekdotas.ErrNotFound
	}
	pqErr, ok := err.(*pq.Error)
	if !ok {
		return err
	}
	switch pqErr.Code {
	case "23503": // foreign-key violation
		return anekdotas.ErrNotFound
	case "23505": // unique violation
		return anekdotas.ErrAlreadyExists
	case "23514": // check violation
		return anekdotas.ErrQuestionsAndAnswersMismatch
	}
	return err
}

// generateBindvars generates string with (numOfElements * elementSize) bindvars separated
// into groups by elementSize bindvars in each group.
//
// Example:
//  generateBindvars(2, 3) -> "(?, ?, ?), (?, ?, ?)"
//  generateBindvars(3, 2) -> "(?, ?), (?, ?), (?, ?)"
func generateBindvars(numOfElements int, elementSize int) string {
	element := "(" + strings.TrimSuffix(strings.Repeat("?, ", elementSize), ", ") + "), "
	return strings.TrimSuffix(strings.Repeat(element, numOfElements), ", ")
}
