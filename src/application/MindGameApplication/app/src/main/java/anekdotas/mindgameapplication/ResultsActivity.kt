package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import anekdotas.mindgameapplication.databinding.ActivityResultsBinding
import anekdotas.mindgameapplication.helpers.Time
import anekdotas.mindgameapplication.objects.TopicsObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import kotlin.random.Random
import android.widget.RatingBar

import android.widget.RatingBar.OnRatingBarChangeListener




class ResultsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        val myUserName = intent.getStringExtra(UserObjectConst.USERNAME)
        binding.tvName.text = myUserName
        val totalQuestions = intent.getIntExtra(UserObjectConst.TOTAL_QUESTIONS, 0)
        val myCorrectAnswers = intent.getIntExtra(UserObjectConst.CORRECT_ANSWERS, 0 )
        binding.tvTime.text = "Time spent: ${UserObjectConst.sessionTimeHours}h  ${UserObjectConst.sessionTimeMinutes}min  ${UserObjectConst.sessionTimeSeconds}sec"
        binding.tvScore.text = "Your score is $myCorrectAnswers / $totalQuestions"
        binding.tvRatingInfo.text = "Please rate this quiz!"

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
    }
}