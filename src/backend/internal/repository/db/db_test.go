package db

import (
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/jmoiron/sqlx"
	"github.com/stretchr/testify/assert"
	"testing"
)

func newRepoWithMockDB(t *testing.T) (*Repo, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New(sqlmock.QueryMatcherOption(sqlmock.QueryMatcherEqual))
	if err != nil {
		t.Fatalf("failed to create mock DB: %v", err)
	}
	return &Repo{db: sqlx.NewDb(db, "sqlmock")}, mock
}

func assertErrorExpectations(t *testing.T, err error, wantErr bool, expectedErrMsg string) {
	if wantErr {
		if assert.Error(t, err) {
			assert.EqualError(t, err, expectedErrMsg)
		}
	} else {
		assert.NoError(t, err)
	}
}
