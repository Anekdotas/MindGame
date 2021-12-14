package db

import (
	"anekdotas"
	"context"
	"fmt"
	"testing"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
)

var ctx = context.Background()

func TestRepo_CreateQuestion(t *testing.T) {
	t.Fatal("FIX ME")
	repo, mock := newRepoWithMockDB(t)
	defer repo.Close()

	testCases := []struct {
		name     string
		topic    string
		question *anekdotas.Question
		dbErr    error

		wantErr        bool
		expectedErrMsg string
	}{
		{
			name:  "DB returns error",
			topic: "t1",
			question: &anekdotas.Question{
				Text:            "q1+",
				CorrectAnswerID: 2,
			},
			dbErr:          assert.AnError,
			wantErr:        true,
			expectedErrMsg: assert.AnError.Error(),
		},
		{
			name:  "insertion executed",
			topic: "t1",
			question: &anekdotas.Question{
				Text:            "q1",
				CorrectAnswerID: 2,
			},
			wantErr: false,
		},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			stmt := fmt.Sprintf(
				`INSERT INTO %s
		(topic_id, text, correct_answer, answers)
		VALUES (
			(SELECT id FROM %s WHERE name=?),
			?,
			?,
			?
		) RETURNING id`,
				QuestionsTableName,
				TopicsTableName,
			)
			query := mock.ExpectQuery(stmt).WithArgs(
				tc.topic,
				tc.question.Text,
				tc.question.CorrectAnswerID,
			)
			if tc.dbErr != nil {
				query.WillReturnError(tc.dbErr)
			} else {
				query.WillReturnRows(sqlmock.NewRows([]string{"id"}).AddRow(1))
			}
			// _, err := repo.CreateQuestionWithAnswers(ctx, tc.topic, tc.question)
			// assertErrorExpectations(t, err, tc.wantErr, tc.expectedErrMsg)
		})
	}
}

func TestRepo_GetQuestionsByTopic(t *testing.T) {
	t.Fatal("FIX ME")
	repo, mock := newRepoWithMockDB(t)
	defer repo.Close()

	testCases := []struct {
		name      string
		topic     string
		questions []*anekdotas.Question
		dbError   error

		wantErr        bool
		expectedErrMsg string
	}{
		{
			name:           "DB returns error",
			topic:          "t1",
			dbError:        assert.AnError,
			wantErr:        true,
			expectedErrMsg: assert.AnError.Error(),
		},
		{
			name:  "select executed",
			topic: "t1",
			questions: []*anekdotas.Question{
				{ID: 1, Text: "q1", CorrectAnswerID: 2},
				{ID: 2, Text: "q2", CorrectAnswerID: 1},
			},
			wantErr: false,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			stmt := fmt.Sprintf(
				"SELECT q.* FROM %s q JOIN %s t ON q.topic_id = t.id WHERE t.name = ?",
				QuestionsTableName, TopicsTableName,
			)
			query := mock.ExpectQuery(stmt).WithArgs(tc.topic)
			if tc.dbError != nil {
				query.WillReturnError(tc.dbError)
			} else {
				rows := sqlmock.NewRows([]string{"id", "text", "media_url", "correct_answer"})
				for _, q := range tc.questions {
					rows.AddRow(q.ID, q.Text, q.MediaURL, q.CorrectAnswerID)
				}
				query.WillReturnRows(rows)
			}
			_, questions, err := repo.GetQuestionsByTopic(ctx, tc.topic, 0)
			assertErrorExpectations(t, err, tc.wantErr, tc.expectedErrMsg)
			for i, question := range questions {
				assert.Equal(t, tc.questions[i].ID, question.ID)
				assert.Equal(t, tc.questions[i].Text, question.Text)
				assert.Equal(t, tc.questions[i].MediaURL, question.MediaURL)
				assert.Equal(t, tc.questions[i].CorrectAnswerID, question.CorrectAnswerID)
			}
		})
	}
}

func TestRepo_UpdateMediaURL(t *testing.T) {
	repo, mock := newRepoWithMockDB(t)
	defer repo.Close()

	testCases := []struct {
		name       string
		questionID int64
		mediaURL   string
		dbError    error

		wantErr        bool
		expectedErrMsg string
	}{
		{
			name:           "DB returns error",
			questionID:     1,
			dbError:        assert.AnError,
			wantErr:        true,
			expectedErrMsg: assert.AnError.Error(),
		},
		{
			name:       "update executed",
			questionID: 1,
			mediaURL:   "https://example.test/image",
			wantErr:    false,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			stmt := fmt.Sprintf("UPDATE %s SET media_url = ? WHERE id = ?", QuestionsTableName)
			query := mock.ExpectExec(stmt).WithArgs(tc.mediaURL, tc.questionID)
			if tc.dbError != nil {
				query.WillReturnError(tc.dbError)
			} else {
				query.WillReturnResult(sqlmock.NewResult(0, 1))
			}
			err := repo.UpdateMediaURL(ctx, tc.questionID, tc.mediaURL)
			assertErrorExpectations(t, err, tc.wantErr, tc.expectedErrMsg)
		})
	}
}
