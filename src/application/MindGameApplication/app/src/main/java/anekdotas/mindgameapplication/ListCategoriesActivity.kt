package anekdotas.mindgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import anekdotas.mindgameapplication.adapters.CategoriesAdapter
import anekdotas.mindgameapplication.adapters.TopicsAdapter
import anekdotas.mindgameapplication.databinding.ActivityListCategoriesBinding
import anekdotas.mindgameapplication.databinding.ActivityListTopicsBinding
import anekdotas.mindgameapplication.objects.CategoriesObject
import anekdotas.mindgameapplication.objects.TopicsObject

class ListCategoriesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListCategoriesBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val categoryListAdapter = CategoriesAdapter(CategoriesObject.categoryList)
        binding.rvCategoryRecycler.adapter = categoryListAdapter
        binding.rvCategoryRecycler.layoutManager = LinearLayoutManager(this)
    }
}