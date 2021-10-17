package com.example.chatinterface2

import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_chat2.*

class ChatActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)

        colorButton.setOnClickListener {
            message1.setText("changed text")
            message1.setBackgroundColor(Color.parseColor("#2196F3"))
//            message1.setTextAppearance(this, R.style.inTest1)
            super.setTheme(R.style.wrapContent_double)     //currently makes no impact
            blueContainer
//            message2.setBackgroundColor(Color.parseColor(ContextCompat.getColor(this, R.color.light_gray).toString()))
            //ContextCompat.getColor(context, R.color.your_color);
//            Log.d("-_- TAG:", .toString())
//            blueContainer.height = "wrap_content"
//            host_message.setTextAppearance(this, R.style.inTest1)
        }
    }
}