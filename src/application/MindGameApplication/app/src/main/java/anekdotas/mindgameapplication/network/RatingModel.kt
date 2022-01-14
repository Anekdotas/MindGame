package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class RatingModel(
    @Json(name="rating")
    val rating: Double = 0.0
)