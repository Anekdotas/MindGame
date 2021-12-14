package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.HostModel
import java.text.SimpleDateFormat
import java.util.*

object HostObject {
    val host = HostModel("Professor Lazgov", //Hostname

        "https://media.discordapp.net/attachments/883320939666890802/912748283078803526/Lasgov.png?width=474&height=671",

        //Good Answers
        listOf("${UserObjectConstTest.currentUser.username} Perhaps this is the start of a correct answer streak?",
            "Acceptable answer",
            "Correct! Finally some good answers!",
            "Was this question difficult? Obviously not if you managed to answer it. But congratulations anyway!",
            "Correct!",
            "Hmm perhaps I have been a bit too harsh, you answered this question correctly"
            ),

        //Bad Answers
        listOf("If your brain was dynamite, there wouldn’t be enough to blow your hat off",
            "Your answers are more disappointing than an unsalted pretzel",
            "This answer is really emotional but it makes no sense to me",
            "I’ll never forget the first time we met. But I’ll keep trying.",
            "You know you remind me of my 2 year old son. You both have the same level of brain capacity",
            "Hold still. I’m trying to imagine you with a brain",
            "You are stupid. And I’m not insulting you anymore with this, I might be just describing you",
            "Wrong!",
            "Wrong again... Don’t get bitter, just get better",
            "Somewhere out there is a tree tirelessly producing oxygen for you. You owe it an apology",
            "Can't answer anything correctly? Well that sounds like a you problem",
            "You have an entire life to be an idiot. Why not take today off?",
            "Maybe if I start telling people their brain is an app, they’ll want to use it.",
            "Aaaaaand you disappoint me yet again"),

        //Random Answers
        listOf(
            "You know some people spell my name as Lazgov and some people spell my name as Lasgov. I hate both of them equally.",
            "Did you know that it’s impossible to hum while holding your nose? I found that out recently and it blew my mind",
            "By the way... ${UserObjectConstTest.currentUser.username}? Really? You had a choice of any word in the world and THAT is the username you chose?"),

        //Random Jokes
        listOf( "Once I saw a student wiping the floor with a cake and when I asked him why he said because it is a sponge cake and started laughing. I decided to fail this him on the course right then and there.",
            "This might be out of nowhere but... One day when I am old I want to die peacefully in my sleep, like my grandfather… Not screaming and yelling like those passengers in his car.",
            "Don’t you hate it when someone answers their own questions? I do.",
            "I know they say that money talks, but all mine says is ‘Goodbye. Hell I am working as a host for a terribly designed quiz application and I got a doctorate degree!",
            "You know I burned 2,000 calories in just one day yesterday! That’s the last time I leave cake in the oven while I nap.",
            "You know how they say 'give a man for a fish and he will eat for a today but teach a man a man to fish and he will eat for his life?' I recently came up with a better one - Build a man a fire and he’ll be warm for a day. Set a man on fire and he’ll be warm for the rest of his life.",
            "Today a man knocked on my door and asked for a small donation towards the local swimming pool. I gave him a glass of water and told him to get the hell out of my property.",
            "Want to hear a joke? Actually I do not care this is my show I will tell it anyway. Four fonts walk into a bar. The bartender says, ‘Hey! We don’t want your type in here! That is how this applications visual design was born.’",
            "I always had a fascination with magic shows. Once I was in a show in Mexico city and the Mexican magician told the audience he will disappear on the count of three. He said, ‘Uno, dos…” and poof! He disappeared without a tres Absolutely fascinating.",
            "Allow me to tell a little joke, So it starts as a blind man walks into a bar… and a table… and a chair… and a stool... and a counter",
            "Have you heard about the new restaurant called ‘Karma?’ I went there a few weeks back and the service was terrible. When I asked why there was nothing on the menu the waitress just said 'you get what you deserve.'",
            "Looking at your results on these quizzes let me tell you one thing. If at first you don’t succeed, then skydiving definitely isn’t for you"
        ),


        //Good Streak Answers
        listOf("Woah you are doing very well here! Let's hope you will not fail on this question...",
            "Congratulations you are actually doing quite well in this quiz",
            "Woah you are on a roll! Perhaps everything you need to accomplish your goals is already in you",
            "I have nothing bad to say, it feels strange. But congratulations! Maybe I have misjudged you."),

        //Bad Streak Answers
        listOf("You are doing terribly but I am not surprised!",
            "When life gives you melons, you might be dyslexic.",
            "Be gentle with yourself. You’re doing the best you can! Or that is what your parents would say, I say that you need to try much harder",
            "Life is hard. After all, it kills you. So in essence, you are my life because you are killing me too with these incorrect answers",
            ""),

        //Greetings
        listOf("Oh, another quiz?? Maybe go outside like a normal person",
        "Yes, yes... We already know the procedure. My name is Lazgov I will be your host blah blah get on with it",
        "It is ${SimpleDateFormat.getTimeInstance().format(Date())}, maybe go to sleep or something instead?",
        "Welcome to your MindGame quiz! As you can see I already had my coffee this morning, let us begin",
        "Well back to it eh? Let us begin right away!"),

        //Move to Next Question Messages
        listOf("Moving on...",
        "But no time to chat, let us move to the next question",
        "Next question!",
        "Moving forward",
        "Alright then",
        "Let us move on to the next question",
        "Anyway let us move to the next question",
        "Next question! And they said ${QuestionsObject.questionList.size} questions would be quick",
        "")
    )
}