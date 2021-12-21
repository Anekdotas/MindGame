package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class TopicModel(
    val id: Int,
    @Json(name="name")
    val topicName: String,
    @Json(name="author")
    val author: String,
    var rating: Double = 3.5,
    val description: String = "let's say Andrej, God forbid, you kill someone",
    val imageUrl: String = "https://193.219.91.103:14656/media/3238849391.jpg",
    val difficulty: Int = 1
)