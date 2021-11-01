package anekdotas

type Question struct {
	ID            int64
	Text          string
	MediaURL      string
	CorrectAnswer int
	Answers       []string
}

type Topic struct {
	ID     int64
	Name   string
	Author string
}

type Category struct {
	ID   int64
	Name string
}
