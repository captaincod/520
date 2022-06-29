package com.example.a520.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.a520.R
import com.example.a520.agents.RuMedia
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.eachText
import it.skrape.selects.html5.p

import android.util.Log
import android.widget.TextView
import com.example.a520.activities.MainActivity.Companion.TAG
import it.skrape.matchers.isInteger
import it.skrape.matchers.isNumeric
import it.skrape.selects.html5.tr


class RuMediaActivity : AppCompatActivity() {

    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ru_media)


        back = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        val paragraphs : ArrayList<String> = intent.getSerializableExtra("paragraphs") as ArrayList<String>
        //val table: MutableList<RuMedia> = mutableListOf()
        val table: MutableList<String> = mutableListOf()
        val targetTable = 2
        var currentTable = 0

        for (i in paragraphs){
            val first = i.toCharArray()[0]
            if (!first.isDigit()){
               currentTable += 1
            }
            else {
                if (currentTable == targetTable){
                    //val splitted = i.split(' ')
                    //val name = splitted.subList(0, splitted.size-2).toString()
                    //val date = i.split(' ')[-1]
                    table.add(i)
                }
                else if (currentTable > targetTable){
                    break
                }
            }
        }

        val textView = findViewById<TextView>(R.id.ru_media_text)
        textView.text = table[-1]




    }




}