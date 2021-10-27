package anekdotas.mindgameapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import anekdotas.mindgameapplication.R
import anekdotas.mindgameapplication.network.TopicModel
import anekdotas.mindgameapplication.objects.TopicsObject

class TopicsAdapter (var topics: List<TopicModel>) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.topic_button, parent, false)
        return TopicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.itemView.apply{
            val button = findViewById<Button>(R.id.btn_topic_selection)
            button.text =  topics[position].topicName
        }
    }

    inner class TopicViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
}