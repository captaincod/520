package com.example.a520

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        val physical = intent.getStringExtra("physical")
        val media = intent.getStringExtra("media")

        
    }
}