package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import anekdotas.mindgameapplication.databinding.ActivityProfileBinding
import anekdotas.mindgameapplication.java.StatisticsAdapter
import anekdotas.mindgameapplication.java.StatisticsRecord
import anekdotas.mindgameapplication.objects.TopicsObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    var statisticsList =  ArrayList<StatisticsRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        actionBarSetup()

        setUserCoinsBalance()

        setStatistics()

        binding.spLanguageSelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (binding.spLanguageSelection.selectedItemPosition) {
                    1 -> {
                        setLocale("en")
                    }
                    2 -> {
                        setLocale("ru")
                    }
                    3 -> {
                        setLocale("lt")
                    }
                }
            }

        }
    }

    private fun setStatistics() {
        statisticsList.add(StatisticsRecord("stat name", "value"))
        statisticsList.add(StatisticsRecord("stat name that is long as hell", "value"))
        statisticsList.add(StatisticsRecord("stat name that is even longer than hell", "long value"))
        statisticsList.add(StatisticsRecord("stat name that is even longer than hell", "value"))

        val adapter = StatisticsAdapter(this, R.layout.statistics_record_element, statisticsList)
        binding.lvStatisticsList.adapter = adapter
    }

    private fun setUserCoinsBalance() {
        binding.tvCoinBalance.setText(UserObjectConst.coins.toString())
    }

//      - - - DEPRECATED - - -
/*    private fun fillStatistics() {
//        var statistics = arrayOf("Coins gained: 500", "Coins spent: 480", "Games played: 32", "Best strike: 7", "Total time spent: 35h", "Topics created: 0", "Average time per topic: 5 min", "Topics played: 80", "Topics rated: 77")
//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
//            this,
//            android.R.layout.simple_list_item_1, statistics
//        )
//        binding.lvStatisticsList.adapter = adapter
//    }     */

    private fun actionBarSetup() {
        binding.actionBar.title.setText(R.string.profile_activity_title)
        binding.actionBar.actionBarBackArrow.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setLocale(lang : String) {
        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)

        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        //Reset Activity
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }


}