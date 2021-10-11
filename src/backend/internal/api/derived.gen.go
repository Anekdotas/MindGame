// Code generated by goderive DO NOT EDIT.

package api

import (
	"anekdotas"
)

// deriveFmapQuestions returns a list where each element of the input list has been morphed by the input function.
func deriveFmapQuestions(f func(*anekdotas.Question) *Question, list []*anekdotas.Question) []*Question {
	out := make([]*Question, len(list))
	for i, elem := range list {
		out[i] = f(elem)
	}
	return out
}
