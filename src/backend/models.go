package anekdotas

import "time"

type Answer struct {
	ID   int64
	Text string
}

type Question struct {
	ID              int64
	Text            string
	MediaURL        string
	CorrectAnswerID int64
	Answers         []*Answer
}

type Topic struct {
	ID          int64
	Name        string
	Description string
	Author      string
	Rating      float32
	ImageURL    string
	Difficulty  int
}

type Category struct {
	ID   int64
	Name string
}

type User struct {
	ID       int64
	Username string
	Email    string
}

type Choice struct {
	QuestionID int64
	AnswerID   int64
}

type GameAnalytics struct {
	GameSessionID int64
	Choices       []*Choice
	TimeSpent     time.Duration
	Streak        uint16
}
