package anekdotas

import (
	"github.com/pkg/errors"
)

var (
	ErrIncorrectPassword           = errors.New("incorrect password")
	ErrNotFound                    = errors.New("not found")
	ErrAlreadyExists               = errors.New("already exists")
	ErrQuestionsAndAnswersMismatch = errors.New("questions and answers mismatch")
	ErrGameSessionAlreadyFinished  = errors.New("game session already finished")
	ErrNegativeCoinsValue          = errors.New("negative coins value not allowed")
)
