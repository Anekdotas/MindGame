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
                binding.email.text.toString().isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "No Email Selected", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.username.text.toString().isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "No Username Selected", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.password.text.toString().isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "No Password Selected", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.passwordRepeat.text.toString().isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "No Password Repeat Selected", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    UserObjectConst.usernameRegister = binding.username.text.toString()
                    UserObjectConst.passwordRegister = binding.password.text.toString()
                    UserObjectConst.passwordRepeatRegister = binding.passwordRepeat.text.toString()
                    UserObjectConst.emailRegister = binding.email.text.toString()
                    Thread.sleep(25)
                    callNetworkSignup()
                    //startActivity(intent)
                    //finish()
                }
            }
        }
    }
    private fun callNetworkSignup() {
        val intent = Intent(this, MainActivity::class.java)
        val clientPOST = ApiClient.apiService.pushPostSignup(RegistrationModel(UserObjectConst.usernameRegister,
            UserObjectConst.emailRegister, UserObjectConst.passwordRegister,UserObjectConst.passwordRepeatRegister,))
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