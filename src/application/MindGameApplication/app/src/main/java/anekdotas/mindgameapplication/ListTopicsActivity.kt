package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import anekdotas.mindgameapplication.adapters.TopicsAdapter
import anekdotas.mindgameapplication.databinding.ActivityListTopicsBinding
import anekdotas.mindgameapplication.objects.TopicsObject
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner


class ListTopicsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListTopicsBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBarSetup()


        val topicListAdapter = TopicsAdapter(TopicsObject.topicList)
        binding.rvTopicRecycler.adapter = topicListAdapter
        binding.rvTopicRecycler.layoutManager = LinearLayoutManager(this)

        //spinner logic
        binding.abTopic.spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (binding.abTopic.spSort.selectedItemPosition) {
                    1 -> {
                        TopicsObject.topicList = TopicsObject.topicList.sortedBy { it.topicName }
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                    2 -> {
                        TopicsObject.topicList = (TopicsObject.topicList.sortedBy { it.rating }).reversed()
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                    3 -> {
                        TopicsObject.topicList = TopicsObject.topicList.sortedBy { it.difficulty }
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }

        }
   }

    private fun actionBarSetup(){
        binding.abTopic.title.text = getString(R.string.Topics)
        binding.abTopic.actionBarTopic.setOnClickListener{
            val intent = Intent(this, ListCategoriesActivity::class.java)
            startActivity(intent)
            finish()
        }
        spinnerSetup()
    }

    private fun spinnerSetup() {
        val coloredSpinner = binding.abTopic.spSort
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.topic_spinner, R.layout.colored_spinner_layout
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        coloredSpinner.adapter = adapter
    }

    private fun spinnerCheck(){
        binding.abTopic.spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (binding.abTopic.spSort.selectedItemPosition) {
                    1 -> {
                        TopicsObject.topicList = TopicsObject.topicList.sortedBy { it.topicName }
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                    2 -> {
                        TopicsObject.topicList = TopicsObject.topicList.sortedBy { it.rating }
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                    3 -> {
                        TopicsObject.topicList = TopicsObject.topicList.sortedBy { it.difficulty }
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
