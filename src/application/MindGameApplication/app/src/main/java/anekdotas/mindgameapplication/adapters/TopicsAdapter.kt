package anekdotas.mindgameapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import anekdotas.mindgameapplication.InfoActivity
import anekdotas.mindgameapplication.MainActivity
import anekdotas.mindgameapplication.QuestionsActivity
import anekdotas.mindgameapplication.R
import anekdotas.mindgameapplication.helpers.NetworkChecker
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.network.TopicModel
import anekdotas.mindgameapplication.objects.CategoriesObject
import anekdotas.mindgameapplication.objects.QuestionsObject
import anekdotas.mindgameapplication.objects.TopicsObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TopicsAdapter (private var topics: List<TopicModel>? = TopicsObject.topicList) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.topic_button, parent, false)
        return TopicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topics!!.size
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.itemView.apply{
            val button = findViewById<Button>(R.id.btn_topic_selection)
            button.text =  topics!![position].topicName
            button.setOnClickListener{Log.d("Buttons", "ButtonClicked")
                if(NetworkChecker.isNetworkAvailable(context)) {
                    TopicsObject.selectedTopic = topics!![position]
                    callNetwork()
                    Thread.sleep(100)
                    context.startActivity(Intent(context, InfoActivity::class.java))
                }
                else{
                    Toast.makeText(context, "No Internet Connection\nPlease restart the application with internet connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun callNetwork() {
        val client = ApiClient.apiService.getProperQuestions("https://193.219.91.103:6524/categories/${CategoriesObject.selectedCategory!!.id}/topics/${TopicsObject.selectedTopic!!.topicName}/questions")
        client.enqueue(object : Callback<List<QuestionModel>> {
            override fun onResponse(call: Call<List<QuestionModel>>, response: Response<List<QuestionModel>>) {
                if(response.isSuccessful){
                    QuestionsObject.questionList = response.body()!!
                    Log.d("url2", ""+response.body())
                    Log.d("url3", ""+QuestionsObject.questionList.toString())
                }
            }
            override fun onFailure(call: Call<List<QuestionModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

    inner class TopicViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
}