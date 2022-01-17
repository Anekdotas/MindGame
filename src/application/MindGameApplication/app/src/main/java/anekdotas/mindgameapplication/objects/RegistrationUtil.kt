package anekdotas.mindgameapplication.objects

import android.util.Log

object RegistrationUtil {
    fun validateRegistrationInput(username : String, password : String, repeatedPassword : String, email : String): Boolean{
        if(password != repeatedPassword){
            return false
        }
        if(!(email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()))){
            Log.e("email: ", "Wrong email type")
            return false
        }
    return true
    }
}