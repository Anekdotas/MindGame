package logic

import (
	"anekdotas"
	"anekdotas/internal/logic/auth"
	"anekdotas/internal/repository"
	"context"
	"fmt"
	"io"
	"os"
	"strings"

	"github.com/pkg/errors"
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

func (l *Logic) GetQuestionsByTopic(ctx context.Context, topic string, userID int64) (int64, []*anekdotas.Question, error) {
	return l.repo.GetQuestionsByTopic(ctx, topic, userID)
}

func (l *Logic) CreateQuestionWithAnswers(ctx context.Context, topic string, question *anekdotas.Question, answers []*anekdotas.Answer) (id int64, err error) {
	return l.repo.CreateQuestionWithAnswers(ctx, topic, question, answers)
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

func (l *Logic) GetAllTopics(ctx context.Context, categoryID int64) ([]*anekdotas.Topic, error) {
	return l.repo.GetTopicsByCategoryID(ctx, categoryID)
}

func (l *Logic) GetRatedTopics(ctx context.Context, userID int64) ([]*anekdotas.Topic, error) {
	return l.repo.GetRatedTopicsByUserID(ctx, userID)
}

func (l *Logic) CreateTopic(ctx context.Context, categoryID int64, authorID int64, topic *anekdotas.Topic) (name string, err error) {
	return l.repo.CreateTopic(ctx, categoryID, authorID, topic)
}

func (l *Logic) GetAllCategories(ctx context.Context) ([]*anekdotas.Category, error) {
	return l.repo.GetCategories(ctx)
}

func (l *Logic) RegisterUser(ctx context.Context, user *anekdotas.User, password string) (int64, error) {
	hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		return 0, err
	}
	return l.repo.CreateUser(ctx, user, hash)
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

func (l *Logic) FinishGameSession(ctx context.Context, userID int64, statistics *anekdotas.Statistics) error {
	return l.repo.UpdateStatistics(ctx, userID, statistics)
}

func (l *Logic) RateTopic(ctx context.Context, userID, topicID int64, rating float32) error {
	return l.repo.RateTopicByID(ctx, userID, topicID, rating)
}
