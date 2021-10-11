package anekdotas.mindgameapplication

object QuestionsObjectConst{

    //Placeholder values
    const val USERNAME = "USER_NAME"
    const val TOTAL_QUESTIONS = "TOTAL_QUESTIONS"
    const val CORRECT_ANSWERS = "CORRECT_ANSWERS"

    // TODO: 10/3/2021 make all this not hardcoded...
    fun getQuestions(): ArrayList<Question>{
        val questionList = ArrayList<Question>()

        val que1 = Question(1,
            "What is the meaning of life?",
            R.drawable.ic_questionmark, // question image
            "Wealth",
            "Power",
            "Happiness",
            "42",
            4
        )

        val que2 = Question(2,
            "Who is Gordon Ramsay?",
            R.drawable.ic_questionmark, // question image
            "Musician",
            "Politician",
            "Chef",
            "42",
            3
        )

        val que3 = Question(3,
            "Who are you?",
            R.drawable.ic_questionmark, // question image
            "Human",
            "Alien",
            "Panda",
            "42",
            4
        )



        questionList.add(que1)
        questionList.add(que2)
        questionList.add(que3)
        return questionList

    }


}