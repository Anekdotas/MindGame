package db

import (
	"anekdotas/internal/repository"
	"fmt"
	"github.com/jmoiron/sqlx"
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