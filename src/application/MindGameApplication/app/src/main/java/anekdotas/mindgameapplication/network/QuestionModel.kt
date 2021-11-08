package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModel (
    val id: Int,
    @Json(name="text")
    val question: String,
    @Json(name="mediaUrl")
    val image: String = "http://193.219.91.103:7537/media/3238849391.jpg",
    @Json(name="correctAnswer")
    val answer: Int,
    @Json(name="answers")
    val options: List<String>,
)