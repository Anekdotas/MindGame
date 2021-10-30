package com.example.bruhtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bruhtest.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val a1 = Message("Host", "Hello there",R.drawable.chuvas_cropped)
        val a2 = Message("User", "Hi?", R.drawable.chuvas_cropped)
//        val a3 = Message("Host", "Right now we are running some tests.",)
//        val a4 = Message("User", "So what should I do?",)
//        val a5 = Message("Host", "Nothing, just chill.",)
//        val a6 = Message("User", "OK, np.",)
//        val a = Message("USER", "TEXT")

        var messageList =  ArrayList<Message>()
        messageList.add(a1)
        messageList.add(a2)
//        messageList.add(a3)
//        messageList.add(a4)
//        messageList.add(a5)
//        messageList.add(a6)
        messageList.add(
            Message(
                "Name Surname Father's Name And Even Longer Name To Avoid Fit In Only One Single Line",
                "Okay, so for better understanding and testing purposes there is a message with a very long and meaningless text." +
                        "This demonstrates how the application handles very long messages.",
                R.drawable.chuvas_cropped
            )
        )


        messageList.add(randomMessage())
        val adapter: ChatAdapter = ChatAdapter(this, R.layout.message_list_view_element, messageList)
        binding.ListView.adapter = adapter

        binding.sendButton.setOnClickListener {
            messageList.add(randomMessage())
            binding.ListView.adapter = adapter
        }
    }

    private fun randomMessage(): Message {
        var possibleAnswers = ArrayList<Message>()
        possibleAnswers.add(Message("*an author*", "*a text*", R.drawable.bred))
        possibleAnswers.add(Message("author1", "message1", R.drawable.chuvas_cropped))
//        possibleAnswers.add(Message("author2", "message2",))
//        possibleAnswers.add(Message("author3", "message3",))
//        possibleAnswers.add(Message("author4", "message4",))
//        possibleAnswers.add(Message("author5", "message5",))
//        possibleAnswers.add(Message("author6", "message6",))
//        possibleAnswers.add(Message("author7", "message7",))
        return possibleAnswers[Random.nextInt(0, possibleAnswers.size)]
    }
}