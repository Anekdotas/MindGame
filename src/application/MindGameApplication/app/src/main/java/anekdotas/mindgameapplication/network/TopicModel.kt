package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class TopicModel (
    val id: Int,
    @Json(name="name")
    val topicName: String,
    @Json(name="author")
    val author: String
)