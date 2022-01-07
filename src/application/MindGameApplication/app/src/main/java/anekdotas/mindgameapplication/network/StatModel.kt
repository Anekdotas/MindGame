package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class StatModel(
    val id : Int,
    val choices : List<ChoiceModel>,
    @Json(name="timeSpent")
    val secondsSpent : Int,
    val streak : Int
)