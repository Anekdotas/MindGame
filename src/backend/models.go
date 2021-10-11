package anekdotas

type Question struct {
	ID               int
	Text             string
	MediaURL         string
	CorrectAnswer    string
	IncorrectAnswers []string
}
