package com.example.a520.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.R
import com.example.a520.agents.RuAgent
import com.example.a520.agents.RuAgentsAdapter
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.eachText
import it.skrape.selects.html5.p

class RuAgentsActivity : AppCompatActivity() {

    lateinit var back: Button
    lateinit var agentsView: RecyclerView
    var agents: MutableList<RuAgent> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ru_agents)

        getData()

        agentsView = findViewById(R.id.agents_view)
        agentsView.layoutManager = LinearLayoutManager(this)
        agentsView.adapter = RuAgentsAdapter(agents)

        back = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }


    }

    private fun getData(){
        val physicalParagraphs = skrape(HttpFetcher) {
            request {
                url = "https://minjust.gov.ru/ru/activity/directions/942/spisok-lic-vypolnyayushih-funkcii-inostrannogo-agenta/"
            }
            extractIt<ListActivity.ScrapeData> {
                htmlDocument {
                    it.paragraphs = p { findAll { eachText } }
                }
            }
        }
        val paras = physicalParagraphs.paragraphs
        for (i in paras) {
            if (i.length in 1..2) {
                val agent = RuAgent(
                    paras[paras.indexOf(i)+1],
                    paras[paras.indexOf(i)+2],
                    paras[paras.indexOf(i)+3],
                    paras[paras.indexOf(i)+4]
                )
                agents.add(agent)
            }
        }
    }
}