package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import anekdotas.mindgameapplication.adapters.TopicsAdapter
import anekdotas.mindgameapplication.databinding.ActivityMainBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.network.TopicModel
import anekdotas.mindgameapplication.objects.QuestionsObject
import anekdotas.mindgameapplication.objects.TopicsObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//REMINDER! IN ANDROID MANIFEST CLEARTEXT COMM IS ENABLED BUT WORKS ONLY IN API 23 AND ABOVE, NEED TO FIX

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        callNetwork()
        callNetworkTopics()
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            if(binding.username.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "No Username Selected", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@MainActivity, "Welcome ${binding.username.text.toString()}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, QuestionsProtoActivity::class.java)
                intent.putExtra(UserObjectConst.USERNAME, binding.username.text.toString()) // sends the username to other activities, delete later
                startActivity(intent)
                finish()
            }
        }
        binding.btnMenu2.setOnClickListener{
            val intent2 = Intent(this, ListTopicsActivity::class.java)
            startActivity(intent2)
            finish()
        }
    }

    private fun callNetwork() {
        val client = ApiClient.apiService.getQuestions()
        client.enqueue(object : Callback<List<QuestionModel>> {
            override fun onResponse(call: Call<List<QuestionModel>>, response: Response<List<QuestionModel>>) {
                if(response.isSuccessful){
                    Log.d("TestSecond! ", ""+response.body())
                    QuestionsObject.questionList = response.body()
                    Log.d("TestBody! ", ""+ QuestionsObject.questionList)
                }
            }
            override fun onFailure(call: Call<List<QuestionModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

    private fun callNetworkTopics() {
        val client = ApiClient.apiService.getTopics()
        client.enqueue(object : Callback<List<TopicModel>> {
            override fun onResponse(call: Call<List<TopicModel>>, response: Response<List<TopicModel>>) {
                if(response.isSuccessful){
                    Log.d("TestTopics! ", ""+response.body())
                    TopicsObject.topicList = response.body()
                    Log.d("TestTopicBody! ", ""+ TopicsObject.topicList)
                }
            }
            override fun onFailure(call: Call<List<TopicModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }
}