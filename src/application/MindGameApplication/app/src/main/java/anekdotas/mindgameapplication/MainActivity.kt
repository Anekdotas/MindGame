package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import io.supabase.postgrest.PostgrestDefaultClient
import java.net.URI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Just some values here for the UI, don't touch unless you want to edit the xmls as well
        val btnMenu = findViewById<Button>(R.id.btn_menu)
        val userName = findViewById<AppCompatEditText>(R.id.username)

        //Displays a little pop up at the bottom of the screen
            btnMenu.setOnClickListener {
                if(userName.text.toString().isEmpty()) {
                    Toast.makeText(this@MainActivity, "No Username Selected", Toast.LENGTH_SHORT).show()
            }
                else{
                    Toast.makeText(this@MainActivity, "Welcome ${userName.text.toString()}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, QuestionsProto::class.java)
                    startActivity(intent)
                    finish()
                }
        }


    }
}