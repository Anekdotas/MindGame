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
    fun giveRandomRandom(): Int {
        return Random.nextInt(0, HostObject.host.randomAnswers.size)
    }
    fun chance(chance : Int) : Boolean {  //chance has to be from 0 to 100
        return Random.nextInt(1, 100)<=chance
    }
}