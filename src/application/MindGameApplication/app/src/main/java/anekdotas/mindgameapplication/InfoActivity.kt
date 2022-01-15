package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import anekdotas.mindgameapplication.databinding.ActivityInfoBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.RatedListModel
import anekdotas.mindgameapplication.objects.*
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInfoBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        callRatedList()
        
        binding.tvTopicName.text = TopicsObject.selectedTopic.topicName
        binding.ivTopic.load(TopicsObject.selectedTopic.imageUrl)
        binding.tvDesc.text = TopicsObject.selectedTopic.description
        binding.rbRating.rating = TopicsObject.selectedTopic.rating.toFloat()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        when (TopicsObject.selectedTopic.difficulty) {
            0 -> {
                binding.llDifficulty.setBackgroundColor(ContextCompat.getColor(this, R.color.good_green))
                binding.tvDifficulty.text = getString(R.string.info_activity_easy)
            }
            1 -> {
                binding.llDifficulty.setBackgroundColor(ContextCompat.getColor(this, R.color.good_yellow))
                binding.tvDifficulty.text = getString(R.string.info_activity_moderate)
            }
            2 -> {
                binding.llDifficulty.setBackgroundColor(ContextCompat.getColor(this, R.color.good_red))
                binding.tvDifficulty.text = getString(R.string.info_activity_hard)
            }
        }


        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, QuestionsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun callRatedList() {
        val client = ApiClient.apiService.getRatedTopics("${Const.ipForNetworking}/categories/${CategoriesObject.selectedCategory!!.id}/topics/rated", "Bearer " + JwtObject.userJwt.token)
        client.enqueue(object : Callback<RatedListModel> {
            override fun onResponse(call: Call<RatedListModel>, response: Response<RatedListModel>) {
                if(response.isSuccessful){
                    Log.d("RatedBody ", ""+ response.body())
                    UserObjectConst.ratedTopicsId = response.body()!!
                    Log.d("RatedBody", UserObjectConst.ratedTopicsId.ids.toString())
                }
            }
            override fun onFailure(call: Call<RatedListModel>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }
}