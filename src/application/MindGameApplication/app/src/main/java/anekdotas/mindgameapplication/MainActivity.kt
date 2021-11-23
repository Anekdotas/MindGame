package anekdotas.mindgameapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import anekdotas.mindgameapplication.databinding.ActivityMainBinding
import anekdotas.mindgameapplication.helpers.NetworkChecker.isNetworkAvailable
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.CategoryModel
import anekdotas.mindgameapplication.network.JwtTestModel
import anekdotas.mindgameapplication.objects.CategoriesObject
import anekdotas.mindgameapplication.objects.JwtObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import anekdotas.mindgameapplication.objects.UserObjectConstTest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        callNetworkCategories()
        setContentView(binding.root)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        val networkOnline = isNetworkAvailable(this)

        binding.tvRegistration.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnTest.setOnClickListener{
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            if(networkOnline){
                when {
                    binding.username.text.toString().isEmpty() -> {
                        Toast.makeText(this@MainActivity, "No Username Selected", Toast.LENGTH_SHORT).show()
                    }
                    binding.password.text.toString().isEmpty() -> {
                        Toast.makeText(this@MainActivity, "No Password Selected", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        UserObjectConst.USERNAME = binding.username.text.toString() //delete later
                        UserObjectConst.PASSWORD = binding.password.text.toString() //delete later
                        UserObjectConstTest.currentUser.username = binding.username.text.toString()
                        UserObjectConstTest.currentUser.password = binding.password.text.toString()

                        callNetworkLogin()
                        Thread.sleep(100)

                    }
                }
            }
            else{
                Toast.makeText(this@MainActivity, "No Internet Connection\nPlease restart the application with internet connection", Toast.LENGTH_LONG).show()
            }
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


    private fun callNetworkLogin() {
        val intent = Intent(this, MainMenuActivity::class.java)
        val clientPOST = ApiClient.apiService.pushPostLogin(UserObjectConstTest.currentUser)
        Log.d("callNetworkPOST", "has been called")
        clientPOST.enqueue(object : Callback<JwtTestModel> {
            override fun onResponse(call: Call<JwtTestModel>, response: Response<JwtTestModel>) {
                if(response.isSuccessful){
                    Log.d("POST response is", ""+ response.body())
                    JwtObject.userJwt = response.body()!!
                    Log.d("JWT stored in memory is", ""+ JwtObject.userJwt)
                    Toast.makeText(this@MainActivity, "Welcome ${binding.username.text.toString()}", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    Log.d("POST did not succeed", "" + response.body())
                    Toast.makeText(this@MainActivity, "Login details incorrect, please try again", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JwtTestModel>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })

    }

}
