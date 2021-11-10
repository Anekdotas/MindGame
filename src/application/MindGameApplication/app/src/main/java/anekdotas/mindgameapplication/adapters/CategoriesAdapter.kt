package anekdotas.mindgameapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import anekdotas.mindgameapplication.ListTopicsActivity
import anekdotas.mindgameapplication.R
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.CategoryModel
import anekdotas.mindgameapplication.network.TopicModel
import anekdotas.mindgameapplication.objects.CategoriesObject
import anekdotas.mindgameapplication.objects.TopicsObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoriesAdapter(var categories: List<CategoryModel>? = CategoriesObject.categoryList) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_button, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories!!.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemView.apply{
            val button = findViewById<Button>(R.id.btn_topic_selection)
            button.text =  categories!![position].categoryName
            button.setOnClickListener{Log.d("Buttons", "ButtonClicked")
                CategoriesObject.selectedCategory = categories!![position]
                callNetworkTopics(CategoriesObject.selectedCategory!!.id)
                Thread.sleep(100)
                context.startActivity(Intent(context, ListTopicsActivity::class.java))
                //TODO (same for "TopicsAdapter")
                // We need to finish() this activity after we
                // open "ListTopicsActivity" as otherwise this activity
                // stays alive and user can return to it by pressing "back"
                // button (the Android default one - which is triangle)
                // P. S. but now user can return back by pressing "back" button
            }
        }
    }

    private fun callNetworkTopics(id : Int) {
        Log.d("Testurl! ", ""+ "https://193.219.91.103:6524/categories/${CategoriesObject.selectedCategory!!.id}/topics")
        val client = ApiClient.apiService.getTopics("https://193.219.91.103:6524/categories/${CategoriesObject.selectedCategory!!.id}/topics")
        client.enqueue(object : Callback<List<TopicModel>> {
            override fun onResponse(call: Call<List<TopicModel>>, response: Response<List<TopicModel>>) {
                if(response.isSuccessful){
                    Log.d("TestTopics! ", ""+ response.body())
                    TopicsObject.topicList = response.body()
                    Log.d("TestTopicBody! ", ""+ TopicsObject.topicList)
                }
            }
            override fun onFailure(call: Call<List<TopicModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

    inner class CategoryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
}