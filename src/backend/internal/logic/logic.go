package logic

import (
	"anekdotas"
	"anekdotas/internal/repository"
	"context"
	"fmt"
	"io"
	"os"
	"strings"
)

type Logic struct {
	repo           repository.Repository
	mediaDir   string
	hostPrefix string
}

func New(repo repository.Repository, mediaDir, hostPrefix string) *Logic {
	return &Logic{repo: repo, mediaDir: mediaDir, hostPrefix: hostPrefix}
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
	mediaURL := strings.TrimPrefix(dst.Name(), l.mediaDir)
	mediaURL = fmt.Sprintf("%s/media%s", l.hostPrefix, mediaURL)
	return l.repo.UpdateMediaURL(ctx, questionID, mediaURL)
}

func (l *Logic) GetAllTopics(ctx context.Context) ([]*anekdotas.Topic, error) {
	return l.repo.GetTopics(ctx)
}

func (l *Logic) CreateTopic(ctx context.Context, topic *anekdotas.Topic) (name string, err error) {
	return l.repo.CreateTopic(ctx, topic)
}
