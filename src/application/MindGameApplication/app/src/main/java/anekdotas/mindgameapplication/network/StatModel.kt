package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class StatModel(
    var id : Int = 0,
    @Json(name="ChoiceModel")
    val choices : MutableList<ChoiceModel>,
    @Json(name="timeSpent")
    var secondsSpent : Int = 0,
    var streak : Int = 0
)