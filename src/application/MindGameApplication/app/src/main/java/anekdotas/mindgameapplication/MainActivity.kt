package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityMainBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.QuestionModel
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
    }

    private fun callNetwork() {
        //ASYNCHRONOUS
            val client = ApiClient.apiService.fetchQuestions()
            client.enqueue(object : Callback<List<QuestionModel>> {
                override fun onResponse(call: Call<List<QuestionModel>>, response: Response<List<QuestionModel>>) {
                    if(response.isSuccessful){
                        Log.d("Success! ", ""+response.body())
                        QuestionsObject.questionList = response.body()
                        Log.d("Test! ", ""+ QuestionsObject.questionList)
                    }
                }
                override fun onFailure(call: Call<List<QuestionModel>>, response: Throwable) {
                    Log.e("Something went wrong! ", ""+response.message)
                }
            })
        }
}