package anekdotas.mindgameapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import anekdotas.mindgameapplication.databinding.ActivityRegisterBinding
import anekdotas.mindgameapplication.helpers.NetworkChecker
import anekdotas.mindgameapplication.network.ApiClient
import anekdotas.mindgameapplication.network.RegistrationModel
import anekdotas.mindgameapplication.objects.RegistrationUtil
import anekdotas.mindgameapplication.objects.UserObjectConst
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
        val networkOnline = NetworkChecker.isNetworkAvailable(this)

        //Displays a little pop up at the bottom of the screen (and goes to the question activity)
        binding.btnMenu.setOnClickListener {
            if(networkOnline) {
                when {
                    binding.email.text.toString().isEmpty() -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.reg_activity_no_email_selected_toast),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    binding.username.text.toString().isEmpty() -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.main_activity_no_username_selected_toast),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    binding.password.text.toString().isEmpty() -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.main_activity_no_password_selected_toast),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    binding.passwordRepeat.text.toString().isEmpty() -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.reg_activity_no_password_repeat_selected_toast),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    else -> {
                        if (RegistrationUtil.validateRegistrationInput(
                                binding.username.text.toString(),
                                binding.password.text.toString(),
                                binding.passwordRepeat.text.toString(),
                                binding.email.text.toString()
                            )
                        ) {
                            UserObjectConst.usernameRegister = binding.username.text.toString()
                            UserObjectConst.passwordRegister = binding.password.text.toString()
                            UserObjectConst.passwordRepeatRegister =
                                binding.passwordRepeat.text.toString()
                            UserObjectConst.emailRegister = binding.email.text.toString()
                            Thread.sleep(25)
                            callNetworkSignup()
                        }
                    }
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
                    Toast.makeText(this@RegisterActivity, getString(R.string.reg_activity_glad_to_have_you_overloaded_toast,binding.username.text.toString()), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    Log.d("POST for signup did not succeed", "" + response.code())
                    Toast.makeText(this@RegisterActivity, getString(R.string.reg_activity_reg_details_incorrect_toast), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })

    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}