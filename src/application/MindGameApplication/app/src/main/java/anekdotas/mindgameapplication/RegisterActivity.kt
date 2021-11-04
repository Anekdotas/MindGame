package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityRegisterBinding
import anekdotas.mindgameapplication.objects.UserObjectConst

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
                    Toast.makeText(
                        this@RegisterActivity,
                        "Welcome ${binding.username.text.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, ListCategoriesActivity::class.java)
                    UserObjectConst.usernameRegister = binding.username.text.toString()
                    intent.putExtra(UserObjectConst.USERNAME, binding.username.text.toString())
                    intent.putExtra(
                        UserObjectConst.passwordRegister,
                        binding.password.text.toString()
                    )// sends the username/password to other activities, delete later
                    Thread.sleep(25)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}