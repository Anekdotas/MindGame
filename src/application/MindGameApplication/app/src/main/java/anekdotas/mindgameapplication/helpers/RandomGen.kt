package anekdotas.mindgameapplication.helpers

import anekdotas.mindgameapplication.objects.HostObject
import kotlin.random.Random

object RandomGen {
    fun giveRandomBad(): Int {
        return Random.nextInt(0, HostObject.host.badAnswers.size)
    }
    fun giveRandomGood(): Int {
        return Random.nextInt(0, HostObject.host.goodAnswers.size)
    }
}