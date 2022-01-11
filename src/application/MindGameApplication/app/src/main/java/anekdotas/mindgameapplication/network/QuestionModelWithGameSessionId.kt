package anekdotas.mindgameapplication.network
import com.squareup.moshi.Json

data class QuestionModelWithGameSessionId (
    val gameSessionId: Int,
    val questions : List<QuestionModel>
)