package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import anekdotas.mindgameapplication.databinding.ActivityResultsBinding
import anekdotas.mindgameapplication.objects.UserObjectConst

// TODO: 10/2/2021  (add a background for the results screen, something that looks proper, right now it uses the
//  menu bg that does no fit well with design)

// TODO: 10/2/2021 (add an original asset for trophy if possible) ; not high priority but would be nice

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myUserName = intent.getStringExtra(UserObjectConst.USERNAME)
        binding.tvName.text = myUserName
        val totalQuestions = intent.getIntExtra(UserObjectConst.TOTAL_QUESTIONS, 0)
        val myCorrectAnswers = intent.getIntExtra(UserObjectConst.CORRECT_ANSWERS, 0 )
        binding.tvScore.text = "Your score is $myCorrectAnswers / $totalQuestions"

        binding.btnFinish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}