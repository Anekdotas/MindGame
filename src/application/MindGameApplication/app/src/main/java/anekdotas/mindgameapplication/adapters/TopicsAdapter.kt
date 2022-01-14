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
import anekdotas.mindgameapplication.network.QuestionModelWithGameSessionId
import anekdotas.mindgameapplication.network.TopicModel
import anekdotas.mindgameapplication.objects.*
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
        val client = ApiClient.apiService.getProperQuestions("${Const.ipForNetworking}/categories/${CategoriesObject.selectedCategory!!.id}/topics/${TopicsObject.selectedTopic!!.topicName}/questions", "Bearer " + JwtObject.userJwt.token)
        client.enqueue(object : Callback<QuestionModelWithGameSessionId> {
            override fun onResponse(call: Call<QuestionModelWithGameSessionId>, response: Response<QuestionModelWithGameSessionId>) {
                if(response.isSuccessful){
                    QuestionsObjectWithGameSessionId.questionsWithGsId = response.body()!!
                    QuestionsObject.questionList = QuestionsObjectWithGameSessionId.questionsWithGsId.questions
                    Log.d("url2", ""+response.body())
                    Log.d("url3", ""+QuestionsObject.questionList.toString())
                }
            }
            override fun onFailure(call: Call<QuestionModelWithGameSessionId>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }
    
    inner class TopicViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
}