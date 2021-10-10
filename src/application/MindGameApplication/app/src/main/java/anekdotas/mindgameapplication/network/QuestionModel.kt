package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModel (
    val id: Int,
    val question: String,
    val image: String,
    val optiona: String,
    val optionb: String,
    val optionc: String,
    val optiond: String,
    val answer: Int
)