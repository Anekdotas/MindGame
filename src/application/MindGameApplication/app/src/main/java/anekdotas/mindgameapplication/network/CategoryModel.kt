package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class CategoryModel (
    val id: Int,
    @Json(name="name")
    val topicName: String
)