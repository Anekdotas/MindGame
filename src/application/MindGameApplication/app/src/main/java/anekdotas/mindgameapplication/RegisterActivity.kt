package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityRegisterBinding
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.JwtTestModel
import anekdotas.mindgameapplication.network.RegistrationModel
import anekdotas.mindgameapplication.objects.JwtObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import anekdotas.mindgameapplication.objects.UserObjectConstTest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding // UI element binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            when {
                binding.username.text.toString().isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "No Username Selected", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.password.text.toString().isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "No Password Selected", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val intent = Intent(this, MainActivity::class.java)
                    UserObjectConst.usernameRegister = binding.username.text.toString()
                    intent.putExtra(UserObjectConst.USERNAME, binding.username.text.toString())
                    Thread.sleep(25)
                    callNetworkSignup()
                    //startActivity(intent)
                    //finish()
                }
            }
        }
    }
    private fun callNetworkSignup() {
        val clientPOST = ApiClient.apiService.pushPostSignup(RegistrationModel(UserObjectConst.usernameRegister,
            "placeholder2@anekdotas.llc", UserObjectConst.passwordRegister,UserObjectConst.passwordRegister,))
        Log.d("callNetworkLogin", "has been called")
        clientPOST.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("POST response code is", ""+ response.code())
                    Toast.makeText(this@RegisterActivity, "Glad to have you ${binding.username.text.toString()}", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    Log.d("POST for signup did not succeed", "" + response.code())
                    Toast.makeText(this@RegisterActivity, "Registration details incorrect, please try again", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })

    }
}