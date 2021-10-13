package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModel (
    val id: Int,
    val question: String,
    val image: String,
    @Json(name="optiona")
    val optionA: String,
    @Json(name="optionb")
    val optionB: String,
    @Json(name="optionc")
    val optionC: String,
    @Json(name="optiond")
    val optionD: String,
    val answer: Int
)