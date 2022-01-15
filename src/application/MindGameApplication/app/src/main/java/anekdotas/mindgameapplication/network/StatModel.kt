package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class StatModel(
    var gameSessionId : Int = 0,
    @Json(name="choices")
    val choices : MutableList<ChoiceModel>,
    var streak : Int = 0,
    @Json(name="timeSpent")
    var secondsSpent : Int = 0,
)