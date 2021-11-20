package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.HostModel

object HostObject {
    val host = HostModel("Professor Lazgov", //Hostname

        //Good Answers
        listOf("${UserObjectConst.USERNAME} you make me happy",
            "acceptable",
            "nice"),

        //Bad Answers
        listOf("If your brain was dynamite, there wouldn’t be enough to blow your hat off",
            "Your answers are more disappointing than an unsalted pretzel",
            "This answer is really emotional but it makes no sense to me",
            "I’ll never forget the first time we met. But I’ll keep trying.",
            "You know you remind me of my 2 year old son. You both have the same level of brain capacity",
            "Hold still. I’m trying to imagine you with a brain",
            "You are stupid. Oh come one please do not be mad, I’m not insulting you, I’m describing you",
            "Wrong!",
            "Wrong again... Don’t get bitter, just get better",
            "Somewhere out there is a tree tirelessly producing oxygen for you. You owe it an apology",
            "Can't answer anything correctly? Well that sounds like a you problem",
            "You have an entire life to be an idiot. Why not take today off?"),

        listOf("Took all the cake",
            "Test Test",
            "Ok then moving on...")
        )
}