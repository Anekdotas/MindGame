package logic

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
	"anekdotas/internal/repository"
	"context"
	"errors"
	"fmt"
	"io"
	"os"
	"strings"

	"golang.org/x/crypto/bcrypt"
)

type Logic struct {
	repo       repository.Repository
	auth       auth.AuthProvider
	mediaDir   string
	hostPrefix string
}

func New(repo repository.Repository, authProvider auth.AuthProvider, mediaDir, hostPrefix string) *Logic {
	return &Logic{
		repo:       repo,
		auth:       authProvider,
		mediaDir:   mediaDir,
		hostPrefix: hostPrefix,
	}
}

func (l *Logic) GetQuestionsByTopic(ctx context.Context, topic string) ([]*anekdotas.Question, error) {
	return l.repo.GetQuestionsByTopic(ctx, topic)
}

func (l *Logic) CreateQuestion(ctx context.Context, topic string, question *anekdotas.Question) (id int64, err error) {
	return l.repo.CreateQuestion(ctx, topic, question)
}

func (l *Logic) SaveMediaFile(ctx context.Context, questionID int64, filename string, src io.Reader) error {
	extension := ""
	if split := strings.Split(filename, "."); len(split) > 0 {
		extension = split[len(split)-1]
	}
	dst, err := os.CreateTemp(l.mediaDir, fmt.Sprintf("*.%s", extension))
	if err != nil {
		return err
	}
	defer dst.Close()
	written, err := io.Copy(dst, src)
	if err != nil {
		return err
	}
	// If nothing was written eventually, then remove newly created empty file
	if written == 0 {
		if err := os.Remove(dst.Name()); err != nil {
			return err
		}
	}
	// TODO: AN-58 - Implement storing raw mediaURL in repository and add prefix to it only when sending to a client
	mediaURL := strings.TrimPrefix(dst.Name(), l.mediaDir)
	mediaURL = fmt.Sprintf("%s/media%s", l.hostPrefix, mediaURL)
	return l.repo.UpdateMediaURL(ctx, questionID, mediaURL)
}

func (l *Logic) GetTopicsByCategory(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error) {
	return l.repo.GetTopicsByCategoryID(ctx, categoryID)
}

func (l *Logic) CreateTopic(ctx context.Context, categoryID int64, topic *anekdotas.Topic) (name string, err error) {
	return l.repo.CreateTopic(ctx, categoryID, topic)
}

func (l *Logic) GetAllCategories(ctx context.Context) ([]*anekdotas.Category, error) {
	return l.repo.GetCategories(ctx)
}

func (l *Logic) AuthenticateUser(ctx context.Context, user *anekdotas.User, password string) (token string, err error) {
	userID, hashedPassword, err := l.repo.GetUserPasswordHash(ctx, user.Username)
	if err != nil {
		return "", err
	}
	if err = bcrypt.CompareHashAndPassword(hashedPassword, []byte(password)); errors.Is(err, bcrypt.ErrMismatchedHashAndPassword) {
		return "", anekdotas.ErrIncorrectPassword
	} else if err != nil {
		return "", err
	}
	return l.auth.NewToken(userID)
}
