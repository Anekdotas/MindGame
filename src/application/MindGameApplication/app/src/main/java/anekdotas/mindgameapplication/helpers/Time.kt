package anekdotas.mindgameapplication.helpers

import anekdotas.mindgameapplication.objects.UserObjectConst
import java.util.concurrent.TimeUnit

object Time {
    fun formatTime(s : Long){
        UserObjectConst.sessionTimeSeconds = TimeUnit.SECONDS.toSeconds(s) % 60
        UserObjectConst.sessionTimeMinutes = TimeUnit.SECONDS.toMinutes(s) % 60
        UserObjectConst.sessionTimeHours = TimeUnit.SECONDS.toHours(s)
   }

    fun resetTime(){
        UserObjectConst.sessionTimeHours = 0
        UserObjectConst.sessionTimeMinutes = 0
        UserObjectConst.sessionTimeSeconds = 0
    }

    fun formatTimeAnalytics(s: Int) : String{
        var sec = (TimeUnit.SECONDS.toSeconds(s.toLong()) % 60).toInt()
        var min = (TimeUnit.SECONDS.toMinutes(s.toLong()) % 60).toInt()
        var h = TimeUnit.SECONDS.toHours(s.toLong()).toInt()
        return "${h}h ${min}m ${sec}s"
    }
}