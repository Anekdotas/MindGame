package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModel (
    val id: Int,
    @Json(name="text")
    val question: String,
    @Json(name="mediaUrl")
    val media: String = "empty",
    @Json(name="correctAnswer")
    val answer: Int,
    @Json(name="answers")
    val options: List<String>,
)