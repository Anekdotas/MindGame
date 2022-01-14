package anekdotas.mindgameapplication

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityMainMenuBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.CategoryModel
import anekdotas.mindgameapplication.objects.CategoriesObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.DialogInterface




class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        callNetworkCategories()


        //PLAY BUTTON logic
        binding.playBtn.setOnClickListener {
            val intent = Intent(this, ListCategoriesActivity::class.java)
            Thread.sleep(100)
            startActivity(intent)
        }

        //SHOP BUTTON logic
        binding.shopBtn.setOnClickListener {
//            val intent = Intent(this, ResultsActivity::class.java)
//            Thread.sleep(100)
//            startActivity(intent)
            Toast.makeText(this, getString(R.string.mainmenu_activity_not_yet_implemented), Toast.LENGTH_SHORT).show()
        }

        //PROFILE BUTTON logic
        binding.personalStatisticsBtn.setOnClickListener {
//            val intent = Intent(this, ResultsActivity::class.java)
//            Thread.sleep(100)
//            startActivity(intent)
            Toast.makeText(this, getString(R.string.mainmenu_activity_not_yet_implemented), Toast.LENGTH_SHORT).show()
        }

        //EXIT BUTTON logic
        binding.exitBtn.setOnClickListener { this.finishAffinity(); }     //simply exits the game

        //LEVEL CREATOR button logic
        binding.btnLevelCreator.setOnClickListener {
            val intent = Intent(this, LevelCreatorTitlePageActivity::class.java)
            Thread.sleep(100)
            startActivity(intent)
        }
    }

    private fun callNetworkCategories() {
        val client = ApiClient.apiService.getCategories()
        client.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(call: Call<List<CategoryModel>>, response: Response<List<CategoryModel>>) {
                if(response.isSuccessful){
                    Log.d("TestCategories! ", ""+ response.body())
                    CategoriesObject.categoryList = response.body()!!
                    Log.d("TestCategoryBody! ", ""+ CategoriesObject.categoryList)
                }
            }
            override fun onFailure(call: Call<List<CategoryModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Closing MindGameApp")
            .setMessage("Are you sure you want to close this application?")
            .setPositiveButton("Yes"
            ) { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}