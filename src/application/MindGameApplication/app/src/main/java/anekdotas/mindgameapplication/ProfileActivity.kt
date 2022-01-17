package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import anekdotas.mindgameapplication.databinding.ActivityProfileBinding
import anekdotas.mindgameapplication.helpers.Time
import anekdotas.mindgameapplication.java.StatisticsAdapter
import anekdotas.mindgameapplication.java.StatisticsRecord
import anekdotas.mindgameapplication.objects.StatObject
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
        binding.tvCoinBalance.text=StatObject.analytics.coins.toString()

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
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_time_spent_playing), Time.formatTimeAnalytics(StatObject.analytics.totalTimeSpent)))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_correct_answers), StatObject.analytics.correctAnswers.toString()))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_correct_answers_percentage), StatObject.analytics.correctAnswersPercentage.toString() + "%"))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_longest_streak), StatObject.analytics.longestStreak.toString()))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_correct_answers_average_game_time),Time.formatTimeAnalytics(StatObject.analytics.averageGameTime)))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_longest_streak_topic), StatObject.analytics.longestStreakTopic))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_topics_created), StatObject.analytics.topicsCreated.toString()))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_topics_rated), StatObject.analytics.topicsRated.toString()))
        statisticsList.add(StatisticsRecord(getString(R.string.profile_activity_topics_played), StatObject.analytics.topicsPlayed.toString()))


        val adapter = StatisticsAdapter(this, R.layout.statistics_record_element, statisticsList)
        binding.lvStatisticsList.adapter = adapter
    }

    private fun setUserCoinsBalance() {
        binding.tvCoinBalance.setText(StatObject.analytics.coins.toString())
    }

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