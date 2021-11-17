package anekdotas

type Question struct {
	ID            int64
	Text          string
	MediaURL      string
	CorrectAnswer int
	Answers       []string
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
