package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.RatingModel
import anekdotas.mindgameapplication.network.TopicModel

object TopicsObject {
    var topicList = listOf(TopicModel(0,"No Internet", "No Internet"))
    var selectedTopic = TopicModel(0,"No Internet", "No Internet")
    var rated = RatingModel()
}