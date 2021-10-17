package com.example.chatinterface2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_chat3.*
import kotlin.random.Random

class ChatActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat3)


//        var id = 0
        colorButton.setOnClickListener {
//            message1.setText("message1")
//            message3.setText("message3")
            moveMessages()
//            id++
            message1.setText(returnRandomAnswer())
        }

    }

    private fun returnRandomAnswer():String {
        val hostAnswers = arrayOf<String>(
            "Correct!",
            "Congrats, let's continue.",
            "Nice try, but it makes no sense for me.",
            "Weird logic, incorrect.",
            "You are talking nonsense.",
            "WOOOHOOO!!!",
            "It is getting more interesting!",
            "Okay, this time you were correct, but can you answer next question?"
        )
        val randomElementNr = Random.nextInt(0, hostAnswers.size)
//        var answersLength = hostAnswers.size.toString()
        return hostAnswers[randomElementNr]
    }

    private fun moveMessages() {

        message7.text = (message6.text.toString())
        message6.text = (message5.text.toString())
        message5.text = (message4.text.toString())
        message4.text = (message3.text.toString())
        message3.text = (message2.text.toString())
        message2.text = (message1.text.toString()  )
//        message1.text = message
    }

}