package anekdotas.mindgameapplication.helpers

import anekdotas.mindgameapplication.objects.HostObject
import anekdotas.mindgameapplication.objects.UserStatsObject
import org.apache.hc.core5.net.Host
import kotlin.random.Random

object HostTalk {
    fun saySomething() : String {
        return when(Random.nextInt(0,3)){
            0 -> HostObject.host.randomAnswers[Random.nextInt(0, HostObject.host.randomAnswers.size)]
            1 -> streak()
            2 -> giveRandomJoke()
            else -> "Moving on..."
        }
    }

    private fun streak() : String {
        return if(UserStatsObject.sessionStreak > 0){
            HostObject.host.goodStreak[Random.nextInt(0, HostObject.host.goodStreak.size)]
        } else{
            HostObject.host.badStreak[Random.nextInt(0, HostObject.host.badStreak.size)]
        }
    }

    fun giveRandomBad(): String {
        return HostObject.host.badAnswers[Random.nextInt(0, HostObject.host.badAnswers.size)]
    }
    fun giveRandomGood(): String {
        return HostObject.host.goodAnswers[Random.nextInt(0, HostObject.host.goodAnswers.size)]
    }
    fun giveRandomGreeting(): String {
        return HostObject.host.greetings[Random.nextInt(0, HostObject.host.greetings.size)]
    }
    fun giveMoveOn(): String {
        return HostObject.host.moveOn[Random.nextInt(0, HostObject.host.moveOn.size)]
    }
    private fun giveRandomJoke(): String{
        return HostObject.host.randomJokes[Random.nextInt(0, HostObject.host.randomJokes.size)]
    }
}