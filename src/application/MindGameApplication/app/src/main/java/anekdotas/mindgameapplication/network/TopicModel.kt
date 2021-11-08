package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class TopicModel (
    val id: Int,
    @Json(name="name")
    val topicName: String,
    @Json(name="author")
    val author: String,
    var rating: Double = 3.5,
    val description: String = "let's say Andrej, God forbid, you kill someone",
    val imageUrl: String = "https://cdn.discordapp.com/attachments/335798016860487682/907239394807791676/download.jpg",
    val difficulty: Int = 0
)