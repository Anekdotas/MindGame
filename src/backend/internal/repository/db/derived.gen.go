// Code generated by goderive DO NOT EDIT.

package db

import (
	"anekdotas"
)

// deriveFmapQRecordToModel returns a list where each element of the input list has been morphed by the input function.
func deriveFmapQRecordToModel(f func(*QuestionRecord) *anekdotas.Question, list []*QuestionRecord) []*anekdotas.Question {
	out := make([]*anekdotas.Question, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}

// deriveFmapTRecordToModel returns a list where each element of the input list has been morphed by the input function.
func deriveFmapTRecordToModel(f func(*TopicRecord) *anekdotas.Topic, list []*TopicRecord) []*anekdotas.Topic {
	out := make([]*anekdotas.Topic, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}