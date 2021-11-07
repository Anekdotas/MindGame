package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityMainBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.objects.UserObjectConst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import anekdotas.mindgameapplication.network.CategoryModel
import anekdotas.mindgameapplication.objects.CategoriesObject

//REMINDER! IN ANDROID MANIFEST CLEARTEXT COMM IS ENABLED BUT WORKS ONLY IN API 23 AND ABOVE, NEED TO FIX

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        callNetworkCategories()
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding.tvRegistration.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            if(binding.username.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "No Username Selected", Toast.LENGTH_SHORT).show()
            }
            else if(binding.password.text.toString().isEmpty()){
                Toast.makeText(this@MainActivity, "No Password Selected", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@MainActivity, "Welcome ${binding.username.text.toString()}", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, ListCategoriesActivity::class.java)     //this intent was used before MainMenu is created, delete if there is no issues found
                val intent = Intent(this, MainMenu::class.java)                       //new intent
                UserObjectConst.USERNAME = binding.username.text.toString()
                intent.putExtra(UserObjectConst.USERNAME, binding.username.text.toString())
                intent.putExtra(UserObjectConst.PASSWORD, binding.password.text.toString())// sends the username/password to other activities, delete later
                Thread.sleep(25)
                startActivity(intent)
                finish()
            }
        }
    }



    private fun callNetworkCategories() {
        val client = ApiClient.apiService.getCategories()
        client.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(call: Call<List<CategoryModel>>, response: Response<List<CategoryModel>>) {
                if(response.isSuccessful){
                    Log.d("TestCategories! ", ""+ response.body())
                    CategoriesObject.categoryList = response.body()
                    Log.d("TestCategoryBody! ", ""+ CategoriesObject.categoryList)
                }
            }
            override fun onFailure(call: Call<List<CategoryModel>>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }
}