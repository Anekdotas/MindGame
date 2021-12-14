package handlers

type Answer struct {
	ID   int64  `json:"id,omitempty"`
	Text string `json:"text"`
}
