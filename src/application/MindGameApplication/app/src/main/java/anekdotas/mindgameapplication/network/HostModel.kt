package anekdotas.mindgameapplication.network

data class HostModel (
    val hostName : String = "Professor Lazgov",
    val goodAnswers: List<String>,
    val badAnswers : List<String>
)