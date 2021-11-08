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

        val a1 = Message("Host", "Hello there", R.drawable.chuvas_cropped, R.drawable.not_a_square)
        val a2 = Message("User", "Hi?", R.drawable.chuvas_cropped, R.drawable.bred)

        var messageList =  ArrayList<Message>()
        messageList.add(a1)
        messageList.add(a2)

        messageList.add(
            Message(
                "Name Surname Father's Name And Even Longer Name To Avoid Fit In Only One Single Line",
                "Okay, so for better understanding and testing purposes there is a message with a very long and meaningless text." +
                        "This demonstrates how the application handles very long messages.",
                R.drawable.chuvas_cropped,
                R.drawable.bred
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
        possibleAnswers.add(Message("*an author*", "*a text*", R.drawable.bred, R.drawable.bred))
        possibleAnswers.add(Message("*an author*", "*a text*", R.drawable.bred, R.drawable.not_a_square))
        possibleAnswers.add(Message("author1", "message1", R.drawable.bred, R.drawable.chuvas_cropped))
        return possibleAnswers[Random.nextInt(0, possibleAnswers.size)]
    }
}