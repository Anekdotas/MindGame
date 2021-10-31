package anekdotas.mindgameapplication.objects

import androidx.lifecycle.MutableLiveData
import anekdotas.mindgameapplication.network.TopicModel

object TopicsObject {
    var topicList : List<TopicModel>? = null
    var selectedTopic : TopicModel? = null
}