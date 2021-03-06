// Code generated by goderive DO NOT EDIT.

package handlers

import (
	"anekdotas"
)

// deriveFmapCategories returns a list where each element of the input list has been morphed by the input function.
func deriveFmapCategories(f func(*anekdotas.Category) *Category, list []*anekdotas.Category) []*Category {
	out := make([]*Category, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapChoicesReverse returns a list where each element of the input list has been morphed by the input function.
func deriveFmapChoicesReverse(f func(*choice) *anekdotas.Choice, list []*choice) []*anekdotas.Choice {
	out := make([]*anekdotas.Choice, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapQuestions returns a list where each element of the input list has been morphed by the input function.
func deriveFmapQuestions(f func(*anekdotas.Question) *Question, list []*anekdotas.Question) []*Question {
	out := make([]*Question, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapAnswers returns a list where each element of the input list has been morphed by the input function.
func deriveFmapAnswers(f func(*anekdotas.Answer) *Answer, list []*anekdotas.Answer) []*Answer {
	out := make([]*Answer, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapAnswersReverse returns a list where each element of the input list has been morphed by the input function.
func deriveFmapAnswersReverse(f func(*Answer) *anekdotas.Answer, list []*Answer) []*anekdotas.Answer {
	out := make([]*anekdotas.Answer, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapUnpackTopics returns a list where each element of the input list has been morphed by the input function.
func deriveFmapUnpackTopics(f func(*anekdotas.Topic) int64, list []*anekdotas.Topic) []int64 {
	out := make([]int64, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapTopics returns a list where each element of the input list has been morphed by the input function.
func deriveFmapTopics(f func(*anekdotas.Topic) *Topic, list []*anekdotas.Topic) []*Topic {
	out := make([]*Topic, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}
