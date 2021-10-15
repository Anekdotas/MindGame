package anekdotas

type Question struct {
	ID               int
	Text             string
	MediaURL         string
	CorrectAnswer    string
	IncorrectAnswers []string
}

type Topic struct {
	ID int
	Name string
	Author string
}
