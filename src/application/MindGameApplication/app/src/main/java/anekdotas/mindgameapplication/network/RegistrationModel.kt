package anekdotas.mindgameapplication.network

data class RegistrationModel(
    val username : String,
    val email : String,
    val password : String,
    val repeatPassword : String
)
