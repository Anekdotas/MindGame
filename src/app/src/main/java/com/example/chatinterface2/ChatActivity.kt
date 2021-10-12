package com.example.chatinterface2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import java.util.*


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        //TODO make displaying only one/two elements from this array
        val colors = arrayOf<String>("Красный", "What if there is a line long as hell and a need to use several lines? Is it going to be ok?",
            "Оранжевый", "Желтый", "Зелёный", "Голубой", "Синий", "Фиолетовый")
        val listView = findViewById<ListView>(R.id.list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colors)

        listView.adapter = adapter

        //- - - TRYING TO EFFECT LINEAR LAYOUT - - -
        //Possible solution: https://stackoverflow.com/questions/11710200/android-programmatically-adding-buttons-to-a-layout
        val myLayout = findViewById<LinearLayout>(R.id.myLinearLayout)

    }
}