package com.example.a520

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import it.skrape.core.htmlDocument
import it.skrape.fetcher.*
import it.skrape.selects.*
import it.skrape.selects.html5.*
import kotlin.collections.emptyList as emptyList1

class ListActivity : AppCompatActivity() {

    lateinit var toMain: Button
    lateinit var getIndividuals: Button
    lateinit var namesList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val physical = intent.getStringExtra("physical")
        val media = intent.getStringExtra("media")

        toMain = findViewById(R.id.main)
        toMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        namesList = findViewById(R.id.names_list)

        getIndividuals = findViewById(R.id.get_individuals)

        getIndividuals.setOnClickListener {
            val physicalParagraphs = skrape(HttpFetcher) {
                request {
                    if (physical != null) {
                        url = physical
                    }
                }
                extractIt<ScrapeData> {
                    htmlDocument {
                        it.paragraphs = p { findAll { eachText } }
                    }
                }
            }
            val paras = physicalParagraphs.paragraphs
            val agents: MutableList<IndividualAgent> = mutableListOf()
            for (i in paras) {
                if (i.length in 1..2) {
                    val agent = IndividualAgent(
                        paras[paras.indexOf(i)+1],
                        paras[paras.indexOf(i)+2],
                        paras[paras.indexOf(i)+3],
                        paras[paras.indexOf(i)+4]
                    )
                    agents.add(agent)
                }
            }

            var text = ""
            for (agent in agents){
                text += "\n${agent.name}\n${agent.inclusionDate}\n${agent.foreignSource}\n"
            }
            namesList.text = text
        }

        val mediaParagraphs = skrape(HttpFetcher) {
            request {
                if (media != null) {
                    url = media
                }
            }
            extractIt<ScrapeData> {
                htmlDocument {
                    it.paragraphs = p { findAll { eachText } }
                }
            }
        }

        Log.d("mytag", mediaParagraphs.toString())




    }

    data class ScrapeData(var paragraphs: List<String> = emptyList1())
    data class IndividualAgent(var name: String, var inclusionDate: String,
                               var foreignSource: String, var info: String)
}