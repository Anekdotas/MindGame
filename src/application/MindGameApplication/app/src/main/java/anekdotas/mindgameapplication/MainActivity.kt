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
import anekdotas.mindgameapplication.network.JwtTestModel
import anekdotas.mindgameapplication.objects.JwtObject

import anekdotas.mindgameapplication.objects.UserObjectConst
import anekdotas.mindgameapplication.objects.UserObjectConstTest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        val networkOnline = isNetworkAvailable(this)
        binding.tvRegistration.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            if(networkOnline){
                when {
                    binding.username.text.toString().isEmpty() -> {
                        Toast.makeText(this@MainActivity, getString(R.string.main_activity_no_username_selected_toast), Toast.LENGTH_SHORT).show()
                    }
                    binding.password.text.toString().isEmpty() -> {
                        Toast.makeText(this@MainActivity, getString(R.string.main_activity_no_password_selected_toast), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
            }
        }
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
                    Toast.makeText(this@MainActivity, getString(R.string.main_activity_welcome_overloaded_toast,binding.username.text.toString()), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    Log.d("POST did not succeed", "" + response.body())
                    Toast.makeText(this@MainActivity, getString(R.string.main_activity_login_details_incorrect_toast), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JwtTestModel>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })

    }

}