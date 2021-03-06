package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModelForLevelCreator (
    val id: Int,
    @Json(name="text")
    val question: String,
    @Json(name="mediaUrl")
    val media: String? = null,
    @Json(name="correctAnswer")
    val answer: Int,
    @Json(name="answers")
    val options: List<AnswerModelForLevelCreator>,
)