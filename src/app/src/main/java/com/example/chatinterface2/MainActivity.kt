package com.example.chatinterface2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goChat.setOnClickListener {
            intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}