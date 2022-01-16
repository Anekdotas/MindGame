package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.R
import anekdotas.mindgameapplication.network.RatedListModel

object UserObjectConst{

    //Placeholder values
    var USERNAME = "USER_NAME"
    var PASSWORD = "PASSWORD"
    var TOTAL_QUESTIONS = "TOTAL_QUESTIONS"
    var CORRECT_ANSWERS = "CORRECT_ANSWERS"

     //VALUES FOR REGISTRATION
    var usernameRegister = "User_Name"
    var passwordRegister = "password"
    var passwordRepeatRegister = "password again"
    var emailRegister = "example@register.kt"

    var sessionTimeSeconds : Long = 0
    var sessionTimeMinutes : Long = 0
    var sessionTimeHours : Long = 0
    var ratedTopicsId = RatedListModel()
    var sessionStreak : Int = 0
    var userPhoto : Int = R.drawable.chuvas_cropped

}