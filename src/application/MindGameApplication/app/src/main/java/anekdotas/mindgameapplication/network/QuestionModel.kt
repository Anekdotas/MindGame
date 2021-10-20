package anekdotas.mindgameapplication.network
import anekdotas.mindgameapplication.R
import com.squareup.moshi.Json

data class QuestionModel (
    val id: Int,
    @Json(name="text")
    val question: String,
    val image: String = "https://media.discordapp.net/attachments/883320939666890802/897182017890942996/244661702_397286278572014_2505698586459368395_n.png",
    @Json(name="correctAnswer")
    val answer: Int,
    @Json(name="answers")
    val options: List<String>,
)