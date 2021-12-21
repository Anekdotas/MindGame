package anekdotas.mindgameapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
        binding.ivTopic.load(TopicsObject.selectedTopic.imageUrl)
        binding.tvDesc.text = TopicsObject.selectedTopic.description
        binding.rbRating.rating = TopicsObject.selectedTopic.rating.toFloat()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        when (TopicsObject.selectedTopic.difficulty) {
            0 -> {
                binding.llDifficulty.setBackgroundColor(ContextCompat.getColor(this, R.color.good_green))
                binding.tvDifficulty.text = "Easy"
            }
            1 -> {
                binding.llDifficulty.setBackgroundColor(ContextCompat.getColor(this, R.color.good_yellow))
                binding.tvDifficulty.text = "Moderate"
            }
            2 -> {
                binding.llDifficulty.setBackgroundColor(ContextCompat.getColor(this, R.color.good_red))
                binding.tvDifficulty.text = "Hard"
            }
        }


        binding.btnPlay.setOnClickListener {
            //val intent = Intent(this, QuestionsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}