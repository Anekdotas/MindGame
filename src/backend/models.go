package anekdotas

type Question struct {
	ID            int
	Text          string
	MediaURL      string
	CorrectAnswer int
	Answers       []string
}

type Topic struct {
	ID     int
	Name   string
	Author string
}
