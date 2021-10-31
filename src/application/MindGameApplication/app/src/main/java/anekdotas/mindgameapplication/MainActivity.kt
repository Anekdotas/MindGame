package anekdotas.mindgameapplication

import android.R.attr
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
import android.R.attr.password

import android.content.SharedPreferences




//REMINDER! IN ANDROID MANIFEST CLEARTEXT COMM IS ENABLED BUT WORKS ONLY IN API 23 AND ABOVE, NEED TO FIX

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        callNetworkTopics()
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            if(binding.username.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "No Username Selected", Toast.LENGTH_SHORT).show()
            }
            else if(binding.password.text.toString().isEmpty()){
                Toast.makeText(this@MainActivity, "No Password Selected", Toast.LENGTH_SHORT).show()
            }
            else {
                val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("username", binding.username.text.toString())
                editor.putString("password", binding.password.text.toString())
                editor.apply()

                Toast.makeText(this@MainActivity, "Welcome ${binding.username.text.toString()}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ListTopicsActivity::class.java)
                UserObjectConst.USERNAME = binding.username.text.toString()
                intent.putExtra(UserObjectConst.USERNAME, binding.username.text.toString())
                intent.putExtra(UserObjectConst.PASSWORD, binding.password.text.toString())// sends the username/password to other activities, delete later
                Thread.sleep(25)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun callNetworkTopics() {
        val client = ApiClient.apiService.getTopics()
        client.enqueue(object : Callback<List<TopicModel>> {
            override fun onResponse(call: Call<List<TopicModel>>, response: Response<List<TopicModel>>) {
                if(response.isSuccessful){
                    Log.d("TestTopics! ", ""+ response.body())
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