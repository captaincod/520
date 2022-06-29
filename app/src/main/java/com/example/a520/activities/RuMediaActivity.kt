package com.example.a520.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.a520.R
import com.example.a520.agents.RuMedia

import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.activities.MainActivity.Companion.TAG
import com.example.a520.agents.RuAgent
import com.example.a520.agents.RuAgentsAdapter
import com.example.a520.agents.RuMediaAdapter
import it.skrape.matchers.isInteger
import it.skrape.matchers.isNumeric
import it.skrape.selects.html5.tr
import java.util.*


class RuMediaActivity : AppCompatActivity() {

    lateinit var back: Button
    lateinit var mediaView: RecyclerView
    lateinit var searchView: SearchView
    var mediaList: MutableList<RuMedia> = mutableListOf()
    var recyclerList: MutableList<RuMedia> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ru_media)


        back = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        val arr = resources.getStringArray(R.array.media_entities)
        for (str in arr){
            val media = str.split(' ', '\t')
            val name = media.slice(1..media.size-2).joinToString(separator = " ")
            mediaList.add(RuMedia(name, media.last()))
        }

        recyclerList.addAll(mediaList)

        mediaView = findViewById(R.id.media_view)
        mediaView.layoutManager = LinearLayoutManager(this)
        mediaView.adapter = RuMediaAdapter(recyclerList)

        searchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    recyclerList.clear()
                    var search = newText.lowercase(Locale.getDefault())
                    for (media in mediaList){
                        if (media.name.lowercase(Locale.getDefault()).contains(search)){
                            recyclerList.add(media)
                        }
                        mediaView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    recyclerList.clear()
                    recyclerList.addAll(mediaList)
                    mediaView.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })

    }




}