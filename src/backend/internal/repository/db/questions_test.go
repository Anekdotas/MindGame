package db

import (
	"anekdotas"
	"context"
	"fmt"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/lib/pq"
	"github.com/stretchr/testify/assert"
	"strings"
	"testing"
)

var ctx = context.Background()

func TestRepo_CreateQuestion(t *testing.T) {
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
				Text:          "q1+",
				CorrectAnswer: 2,
				Answers:       []string{"a1", "a2"},
			},
			dbErr:          assert.AnError,
			wantErr:        true,
			expectedErrMsg: assert.AnError.Error(),
		},
		{
			name:  "insertion executed",
			topic: "t1",
			question: &anekdotas.Question{
				Text:          "q1",
				CorrectAnswer: 2,
				Answers:       []string{"a1", "a2"},
			},
			wantErr: false,
		},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			var stringArray pq.StringArray = tc.question.Answers
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
				tc.question.CorrectAnswer,
				stringArray,
			)
			if tc.dbErr != nil {
				query.WillReturnError(tc.dbErr)
			} else {
				query.WillReturnRows(sqlmock.NewRows([]string{"id"}).AddRow(1))
			}
			_, err := repo.CreateQuestion(ctx, tc.topic, tc.question)
			assertErrorExpectations(t, err, tc.wantErr, tc.expectedErrMsg)
		})
	}
}

func TestRepo_GetQuestionsByTopic(t *testing.T) {
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
				{ID: 1, Text: "q1", CorrectAnswer: 2, Answers: []string{"a1", "a2"}},
				{ID: 2, Text: "q2", CorrectAnswer: 1, Answers: []string{"a1", "a2", "a3"}},
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
				rows := sqlmock.NewRows([]string{"id", "text", "media_url", "correct_answer", "answers"})
				for _, q := range tc.questions {
					rows.AddRow(q.ID, q.Text, q.MediaURL, q.CorrectAnswer, "{"+strings.Join(q.Answers, ",")+"}")
				}
				query.WillReturnRows(rows)
			}
			questions, err := repo.GetQuestionsByTopic(ctx, tc.topic)
			assertErrorExpectations(t, err, tc.wantErr, tc.expectedErrMsg)
			for i, question := range questions {
				assert.Equal(t, tc.questions[i].ID, question.ID)
				assert.Equal(t, tc.questions[i].Text, question.Text)
				assert.Equal(t, tc.questions[i].MediaURL, question.MediaURL)
				assert.Equal(t, tc.questions[i].CorrectAnswer, question.CorrectAnswer)
				assert.Equal(t, tc.questions[i].Answers, question.Answers)
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
			name: "update executed",
			questionID: 1,
			mediaURL: "https://example.test/image",
			wantErr: false,
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
