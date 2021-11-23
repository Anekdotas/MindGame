package anekdotas

import (
	"github.com/pkg/errors"
)

var (
	ErrIncorrectPassword = errors.New("incorrect password")
	ErrNotFound          = errors.New("not found")
	ErrAlreadyExists     = errors.New("already exists")
)
