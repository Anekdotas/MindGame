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
import anekdotas.mindgameapplication.network.AnalyticModel
import anekdotas.mindgameapplication.objects.StatObject
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
        binding.tvCoinCount.text=UserObjectConst.coins.toString()

        actionBarSetup()

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
        statisticsList.add(StatisticsRecord("Total Time Spent: ", StatObject.analytics.totalTimeSpent.toString()))
        statisticsList.add(StatisticsRecord("Correct Answers: ", StatObject.analytics.correctAnswers.toString()))
        statisticsList.add(StatisticsRecord("Correct Answers Percentage: ", StatObject.analytics.correctAnswersPercentage.toString() + "%"))
        statisticsList.add(StatisticsRecord("Longest Streak ", StatObject.analytics.longestStreak.toString()))

        statisticsList.add(StatisticsRecord("Average Game Time: ", StatObject.analytics.averageGameTime.toString()))
        statisticsList.add(StatisticsRecord("Topics Created: ", StatObject.analytics.topicsCreated.toString()))
        statisticsList.add(StatisticsRecord("Topics Rated: ", StatObject.analytics.topicsRated.toString()))
        statisticsList.add(StatisticsRecord("Topics Played: ", StatObject.analytics.topicsPlayed.toString()))


        val adapter = StatisticsAdapter(this, R.layout.statistics_record_element, statisticsList)
        binding.lvStatisticsList.adapter = adapter
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