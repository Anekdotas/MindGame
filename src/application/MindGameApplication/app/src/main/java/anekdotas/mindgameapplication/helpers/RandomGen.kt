package anekdotas.mindgameapplication.helpers

import anekdotas.mindgameapplication.objects.HostObject
import kotlin.random.Random

object RandomGen {
    fun chance(chance : Int) : Boolean {  //chance has to be from 0 to 100
        return Random.nextInt(1, 100)<=chance
    }
}