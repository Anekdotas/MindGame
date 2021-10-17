package com.example.chatinterface2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.chatinterface2.databinding.ActivityChat4Binding

class ChatActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityChat4Binding
    private var messageHistory = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChat4Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonSend.setOnClickListener {            //"Send" button's logic
            val enteredText = binding.enterMessage.text.toString()      //Read user input (ignores cases when there is no input)
            binding.displayLastMessage.text = enteredText
            messageHistory.add(enteredText)                             //Save that text to message history
            displayMessages()
        }

    }

    private fun displayMessages() {                   //used to display all messages
                val listView = binding.listActivity4
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageHistory)

                listView.adapter = adapter      //displays all messages
    }
}