package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.AnswerModel
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.network.QuestionModelWithGameSessionId

object QuestionsObjectWithGameSessionId {
    var questionsWithGsId = QuestionModelWithGameSessionId(0, listOf(QuestionModel(0,"Is there internet connection?","",1,
        listOf(AnswerModel(0,"yes"), AnswerModel(1,"no"), AnswerModel(2,"Maybe"),AnswerModel(3,"surely")))
    ))
}