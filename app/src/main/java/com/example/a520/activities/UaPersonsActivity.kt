package com.example.a520.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.R
import com.example.a520.agents.RuMedia
import com.example.a520.agents.RuMediaAdapter
import com.example.a520.agents.UaPersonsAdapter
import java.util.*

class UaPersonsActivity : AppCompatActivity() {

    lateinit var back: Button
    lateinit var personsView: RecyclerView
    lateinit var searchView: SearchView
    lateinit var personsList: Array<String>
    var recyclerList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ua_persons)

        back = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        personsList = resources.getStringArray(R.array.ua_persons)
        recyclerList.addAll(personsList)

        personsView = findViewById(R.id.ua_persons_view)
        personsView.layoutManager = LinearLayoutManager(this)
        personsView.adapter = UaPersonsAdapter(recyclerList)

        searchView = findViewById(R.id.search_ua_persons)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    recyclerList.clear()
                    var search = newText.lowercase(Locale.getDefault())
                    for (person in personsList){
                        if (person.lowercase(Locale.getDefault()).contains(search)){
                            recyclerList.add(person)
                        }
                        personsView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    recyclerList.clear()
                    recyclerList.addAll(personsList)
                    personsView.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })

    }
}