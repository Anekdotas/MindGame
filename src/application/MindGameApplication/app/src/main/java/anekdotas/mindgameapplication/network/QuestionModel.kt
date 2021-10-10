package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModel (
    @Json(name = "qID")
    val id: Int,
    @Json(name = "question")
    val question: String,
    val image: Int,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: Int
)

data class QuestionResponse(@Json(name="results")
val result : List<QuestionModel>)