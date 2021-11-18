package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import anekdotas.mindgameapplication.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        //PLAY BUTTON logic
        binding.playBtn.setOnClickListener {
            val intent = Intent(this, ListCategoriesActivity::class.java)
            startActivity(intent)
        }

        //SHOP BUTTON logic
        binding.shopBtn.setOnClickListener {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }

        //PROFILE BUTTON logic
        binding.personalStatisticsBtn.setOnClickListener {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }

        //EXIT BUTTON logic
        binding.exitBtn.setOnClickListener { finish() }     //simply exits the game
    }
}