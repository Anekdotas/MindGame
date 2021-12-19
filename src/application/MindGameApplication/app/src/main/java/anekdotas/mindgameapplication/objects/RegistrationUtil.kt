package anekdotas.mindgameapplication.objects

object RegistrationUtil {
    fun validateRegistrationInput(username : String, password : String, repeatedPassword : String, email : String): Boolean{
        if(password != repeatedPassword){
            return false
        }
        else if(password.count()<=20){
            return true
        }
        return true
    }
}