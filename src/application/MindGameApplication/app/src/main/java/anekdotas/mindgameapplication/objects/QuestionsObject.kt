package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.QuestionModel

object QuestionsObject {
    var questionList = listOf(QuestionModel(0,"Is there internet connection?", "", 1,
        listOf("No", "Yes", "Maybe", "I don't know")))
}