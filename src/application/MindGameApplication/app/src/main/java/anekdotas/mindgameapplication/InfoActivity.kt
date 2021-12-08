package anekdotas.mindgameapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import anekdotas.mindgameapplication.databinding.ActivityInfoBinding
import anekdotas.mindgameapplication.databinding.ActivityMainBinding
import anekdotas.mindgameapplication.objects.TopicsObject
import coil.load

class InfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInfoBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        
        binding.tvTopicName.text = TopicsObject.selectedTopic.topicName
        binding.ivTopic.load("https://193.219.91.103:6524/media/3238849391.jpg")
        binding.tvDesc.text = TopicsObject.selectedTopic.description
        binding.rbRating.rating = TopicsObject.selectedTopic.rating.toFloat()

        when (TopicsObject.selectedTopic.difficulty) {
            0 -> {
                binding.cvDifficulty.setCardBackgroundColor(Color.rgb(119,221,119))
                binding.tvDifficulty.text = "Easy"
            }
            1 -> {
                binding.cvDifficulty.setCardBackgroundColor(Color.rgb(253, 253, 150))
                binding.tvDifficulty.text = "Moderate"
            }
            2 -> {
                binding.cvDifficulty.setCardBackgroundColor(Color.rgb(255, 105, 97))
                binding.tvDifficulty.text = "Hard"
            }
        }


        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, QuestionsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}