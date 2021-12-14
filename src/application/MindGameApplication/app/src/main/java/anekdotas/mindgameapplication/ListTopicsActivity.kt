package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import anekdotas.mindgameapplication.adapters.TopicsAdapter
import anekdotas.mindgameapplication.databinding.ActivityListTopicsBinding
import anekdotas.mindgameapplication.objects.TopicsObject


class ListTopicsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListTopicsBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        val topicListAdapter = TopicsAdapter(TopicsObject.topicList)
        binding.rvTopicRecycler.adapter = topicListAdapter
        binding.rvTopicRecycler.layoutManager = LinearLayoutManager(this)


    }
}
