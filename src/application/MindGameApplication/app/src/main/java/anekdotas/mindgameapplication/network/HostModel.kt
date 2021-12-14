package anekdotas.mindgameapplication.network

data class HostModel (
    val hostName : String = "Professor Lazgov",
    val hostImage : String = "https://media.discordapp.net/attachments/883320939666890802/912748283078803526/Lasgov.png?width=474&height=671",
    val goodAnswers: List<String>,
    val badAnswers : List<String>,
    val randomAnswers : List<String>,
    val randomJokes : List<String>,
    val goodStreak : List<String>,
    val badStreak : List<String>,
    val greetings : List<String>,
    val moveOn : List<String>
)