package anekdotas.mindgameapplication.helpers

import anekdotas.mindgameapplication.objects.HostObject
import anekdotas.mindgameapplication.objects.UserStatsObject
import kotlin.random.Random

object HostTalk {
    fun saySomething() : String {
        return when(Random.nextInt(0,2)){
            0 -> HostObject.host.randomAnswers[Random.nextInt(0, HostObject.host.randomAnswers.size)]
            1 -> streak()
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
}