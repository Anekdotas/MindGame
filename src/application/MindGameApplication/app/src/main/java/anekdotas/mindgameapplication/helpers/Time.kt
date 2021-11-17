package anekdotas.mindgameapplication.helpers

import anekdotas.mindgameapplication.objects.UserObjectConst
import java.util.concurrent.TimeUnit

object Time {
    fun formatTime(s : Long){
        UserObjectConst.sessionTimeHours = TimeUnit.SECONDS.toHours(s)
        UserObjectConst.sessionTimeMinutes =TimeUnit.SECONDS.toMinutes(s)
        UserObjectConst.sessionTimeHours = TimeUnit.SECONDS.toSeconds(s)
    }
}