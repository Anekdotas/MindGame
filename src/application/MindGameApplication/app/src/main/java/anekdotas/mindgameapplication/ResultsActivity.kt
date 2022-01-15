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
import anekdotas.mindgameapplication.network.*
import anekdotas.mindgameapplication.objects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random.Default.nextInt

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN

        binding.tvName.text = UserObjectConst.USERNAME
        val totalQuestions = intent.getIntExtra(UserObjectConst.TOTAL_QUESTIONS, 0)
        val myCorrectAnswers = intent.getIntExtra(UserObjectConst.CORRECT_ANSWERS, 0 )
        binding.tvTime.text = getString(R.string.results_activity_time_spent_overloaded,UserObjectConst.sessionTimeHours,UserObjectConst.sessionTimeMinutes,UserObjectConst.sessionTimeSeconds)
        binding.tvScore.text = getString(R.string.results_activity_your_score_overloaded,myCorrectAnswers, totalQuestions)
        binding.tvRatingInfo.text = getString(R.string.results_activity_pls_rate_quiz)
        binding.tvCoins.text = earnCoins(myCorrectAnswers).toString() + getString(R.string.results_activity_coins_gained)
        println(UserObjectConst.coins)


        if(StatObject.stats.choices[0].questionId==0){StatObject.stats.choices.removeAt(0)}
        StatObject.stats.gameSessionId=QuestionsObjectWithGameSessionId.questionsWithGsId.gameSessionId
        Log.d("statscheckcorrect", QuestionsObject.questionList.toString())
        Log.d("stats", StatObject.stats.toString())

        callNetworkUploadStats()

        binding.btnFinish.setOnClickListener{
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }

        setRatingUI()

        restartStats()

        binding.btnRetry.setOnClickListener{
            callNetworkRestartQuestions()
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(this, InfoActivity::class.java))
            finish()
        }
    }

    private fun callNetworkUploadStats() {

            val clientPOST = ApiClient.apiService.postStats("${Const.ipForNetworking}/sessions/finish","Bearer " + JwtObject.userJwt.token,
                StatModel(StatObject.stats.gameSessionId, StatObject.stats.choices, StatObject.stats.secondsSpent, StatObject.stats.streak))
            Log.d("callNetworkUploadStats", "${Const.ipForNetworking}/sessions/finish")
            Log.d("ChoiceModel", StatObject.stats.choices.toString())
            clientPOST.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        Log.d("StatPost Done! Woohoo!", ""+ response.code())
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

    private fun setRatingUI(){
            if (TopicsObject.selectedTopic.id in UserObjectConst.ratedTopicsId.ids!!) {
                binding.rbRating.isFocusable = false
                binding.rbRating.setIsIndicator(true)
                binding.rbRating.rating = TopicsObject.selectedTopic.rating.toFloat()
                binding.tvRatingInfo.text = ""
                binding.rbRating.visibility = View.GONE
            }
            else{
                binding.rbRating.onRatingBarChangeListener =
                    OnRatingBarChangeListener { ratingBar, rating, _ -> if (rating < 1.0f) ratingBar.rating = 1.0f
                        val x=binding.rbRating.rating
                        binding.tvRatingInfo.text=getString(R.string.results_activity_thanks_for_rating)
                        callRateTopic(x.toDouble())
                        binding.rbRating.isFocusable = false
                        binding.rbRating.setIsIndicator(true)
                    }
            }
        }

    private fun restartStats(){
        Thread.sleep(100)
        StatObject.stats.choices.clear()
        StatObject.stats.choices.add(ChoiceModel(0,0))
        StatObject.stats.gameSessionId=0
        StatObject.stats.secondsSpent=0
        StatObject.stats.streak=0
    }

    private fun earnCoins(correctAnswers : Int) : Int{
        val earnings = correctAnswers*(nextInt(1, 3))
        UserObjectConst.coins += earnings
        return earnings
    }

    private fun callNetworkRestartQuestions() {
        val client = ApiClient.apiService.getProperQuestions("${Const.ipForNetworking}/categories/${CategoriesObject.selectedCategory!!.id}/topics/${TopicsObject.selectedTopic!!.topicName}/questions", "Bearer " + JwtObject.userJwt.token)
        client.enqueue(object : Callback<QuestionModelWithGameSessionId> {
            override fun onResponse(call: Call<QuestionModelWithGameSessionId>, response: Response<QuestionModelWithGameSessionId>) {
                if(response.isSuccessful){
                    QuestionsObjectWithGameSessionId.questionsWithGsId = response.body()!!
                    QuestionsObject.questionList = QuestionsObjectWithGameSessionId.questionsWithGsId.questions
                }
            }
            override fun onFailure(call: Call<QuestionModelWithGameSessionId>, response: Throwable) {}
        })
    }


    private fun callRateTopic(rating : Double) {

        val clientPOST = ApiClient.apiService.postRating("${Const.ipForNetworking}/categories/${CategoriesObject.selectedCategory!!.id}/topics/${TopicsObject.selectedTopic.id}/rate",
            "Bearer " + JwtObject.userJwt.token,
            RatingModel(rating))
        clientPOST.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("RatePost Done! Woohoo!", ""+ response.code())
                }
                else {
                    Log.d("RatePost failed. Bruh, ${RatingModel(rating)}", "" + response.code())
                }
            }
            override fun onFailure(call: Call<Void>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }
}