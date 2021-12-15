package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityLevelCreatorTitlePageBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.TopicModel
import anekdotas.mindgameapplication.objects.CategoriesObject
import anekdotas.mindgameapplication.objects.TopicsObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LevelCreatorTitlePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLevelCreatorTitlePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelCreatorTitlePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        callNetworkTopics()

        //ActionBar setup
        binding.actionBar.title.setText(R.string.level_creator_title)
        binding.actionBar.actionBarBackArrow.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

//        /* Function for testing */ fillRequiredData()

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, LevelCreatorActivity::class.java)
            var nameTaken = false
            for (item in TopicsObject.topicList) {
                if(item.topicName.uppercase().equals(binding.etTopicName.text.toString().uppercase()))
                    nameTaken = true
            }
            when {
               nameTaken -> {
                   Toast.makeText(this@LevelCreatorTitlePageActivity, "Topic Name is Already Taken", Toast.LENGTH_SHORT).show()
               }
                binding.etTopicName.text.toString().isEmpty() -> {
                    Toast.makeText(this@LevelCreatorTitlePageActivity, "No Topic Name Selected", Toast.LENGTH_SHORT).show()
                }
                binding.etTopicDescription.text.toString().isEmpty() -> {
                    Toast.makeText(this@LevelCreatorTitlePageActivity, "No Topic Description Selected", Toast.LENGTH_SHORT).show()
                }
                nameTaken -> {
                    Toast.makeText(this@LevelCreatorTitlePageActivity, "Topic Name is Already Taken", Toast.LENGTH_SHORT).show()
                }
            else -> {
                intent.putExtra("topicName", binding.etTopicName.text.toString())
                intent.putExtra("topicDesc", binding.etTopicDescription.text.toString())
                intent.putExtra("topicDifficulty", binding.spDifficulty.selectedItemPosition)
                startActivity(intent)
                finish()
                }
            }
        }
    }


    private fun callNetworkTopics() {
        val client = ApiClient.apiService.getTopics("https://193.219.91.103:6524/categories/1/topics")
        client.enqueue(object : Callback<List<TopicModel>> {
            override fun onResponse(call: Call<List<TopicModel>>, response: Response<List<TopicModel>>) {
                if(response.isSuccessful){
                    Log.d("TestTopics! ", ""+ response.body())
                    TopicsObject.topicList = response.body()!!
                    Log.d("TestTopicBody! ", ""+ TopicsObject.topicList)
                }
            }
            override fun onFailure(call: Call<List<TopicModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

    // - - - - - - - - FUNCTIONS USED FOR TESTING - - - - - - - - - -
    private fun fillRequiredData() {
        binding.etTopicName.setText("Topic name")
        binding.etTopicDescription.setText("A brief description")
        binding.spDifficulty.setSelection(2)
    }
}