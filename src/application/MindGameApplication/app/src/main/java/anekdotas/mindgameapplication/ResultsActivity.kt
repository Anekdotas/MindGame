package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import anekdotas.mindgameapplication.databinding.ActivityResultsBinding

import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import anekdotas.mindgameapplication.network.*
import anekdotas.mindgameapplication.objects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN

        binding.tvName.text = UserObjectConstTest.currentUser.username
        val totalQuestions = intent.getIntExtra(UserObjectConst.TOTAL_QUESTIONS, 0)
        val myCorrectAnswers = intent.getIntExtra(UserObjectConst.CORRECT_ANSWERS, 0 )
        binding.tvTime.text = "Time spent: ${UserObjectConst.sessionTimeHours}h  ${UserObjectConst.sessionTimeMinutes}min  ${UserObjectConst.sessionTimeSeconds}sec"
        binding.tvScore.text = "Your score is $myCorrectAnswers / $totalQuestions"
        binding.tvRatingInfo.text = "Please rate this quiz!"

        StatObject.stats.choices.removeAt(0)
        StatObject.stats.id=QuestionsObjectWithGameSessionId.questionsWithGsId.gameSessionId
        Log.d("statscheckcorrect", QuestionsObject.questionList.toString())
        Log.d("stats", StatObject.stats.toString())

        binding.btnFinish.setOnClickListener{
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }

        if(TopicsObject.selectedTopic.id in UserObjectConst.ratedTopicsId){
            binding.rbRating.isFocusable = false
            binding.rbRating.setIsIndicator(true)
            binding.rbRating.rating = TopicsObject.selectedTopic.rating.toFloat()
            binding.tvRatingInfo.text=""
            binding.rbRating.visibility = View.GONE
        }
        else{
            binding.rbRating.onRatingBarChangeListener =
                OnRatingBarChangeListener { ratingBar, rating, _ -> if (rating < 1.0f) ratingBar.rating = 1.0f
                    var x=binding.rbRating.rating
                    binding.tvRatingInfo.text="Thank you for rating!"
                    UserObjectConst.ratedTopicsId.add(TopicsObject.selectedTopic.id)
                    binding.rbRating.isFocusable = false
                    binding.rbRating.setIsIndicator(true)
                }
        }

        callNetworkUploadStats()
    }

    private fun callNetworkUploadStats() {

            val clientPOST = ApiClient.apiService.postStats("https://193.219.91.103:14656/sessions/finish","Bearer " + JwtObject.userJwt.token,
                StatModel(StatObject.stats.id, StatObject.stats.choices, StatObject.stats.secondsSpent, StatObject.stats.streak))
            Log.d("callNetworkUploadStats", "has been called")
            Log.d("ChoiceModel", StatObject.stats.choices.toString())
            clientPOST.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        Log.d("StatPost Done! Woohoo!", ""+ response.code())
                        startActivity(intent)
                    }
                    else {
                        Log.d("StatPost failed. Bruh", "" + response.code())
                    }
                }
                override fun onFailure(call: Call<Void>, response: Throwable) {
                    Log.e("Something went wrong! ", ""+response.message)
                }
            })
    }
}