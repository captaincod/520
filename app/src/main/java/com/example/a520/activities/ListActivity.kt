package com.example.a520.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.example.a520.dialogs.ConnectionDialog
import com.example.a520.R
import com.example.a520.activities.MainActivity.Companion.TAG
import com.example.a520.agents.RuAgent
import com.example.a520.agents.RuMedia
import it.skrape.core.htmlDocument
import it.skrape.fetcher.*
import it.skrape.matchers.isInteger
import it.skrape.matchers.isNumeric
import it.skrape.selects.*
import it.skrape.selects.html5.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.emptyList as emptyList

class ListActivity : AppCompatActivity() {

    lateinit var toMain: Button
    lateinit var progressBar: ProgressBar
    lateinit var getRuAgents: Button
    //lateinit var getRuMedia: Button
    //lateinit var toCompanies: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        toMain = findViewById(R.id.main)
        toMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        progressBar = findViewById(R.id.progress_bar)

        /*
        toCompanies = findViewById(R.id.to_companies)
        toCompanies.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://rozetked.me/cancellations/"))
            startActivity(browserIntent)
        }
         */

        getRuAgents = findViewById(R.id.get_ru_agents)
        getRuAgents.setOnClickListener {
            if (!isConnected()){
                ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
            } else {
                progressBar.visibility = View.VISIBLE
                startActivity(Intent(this, RuAgentsActivity::class.java))
            }
        }

        //getRuMedia = findViewById(R.id.get_ru_media)

    }

    data class ScrapeData(var paragraphs: List<String> = emptyList())


    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    suspend fun getData(): ScrapeData {
        val mediaParagraphs = skrape(HttpFetcher) {
            request {
                url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D0%B8%D0%BD%D0%BE%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%BD%D1%8B%D1%85_%D0%B0%D0%B3%D0%B5%D0%BD%D1%82%D0%BE%D0%B2_(%D0%A0%D0%BE%D1%81%D1%81%D0%B8%D1%8F)"
                timeout = 15000
            }
            extractIt<ListActivity.ScrapeData> {
                htmlDocument {
                    it.paragraphs = tr { findAll { eachText } }
                }
            }
        }
        return mediaParagraphs

    }

    @DelicateCoroutinesApi
    fun getMedia(view: android.view.View) {
        progressBar.visibility = View.VISIBLE
        var paras: List<String>
        GlobalScope.launch (Dispatchers.IO) {
            val mediaParagraphs = getData()
            paras = mediaParagraphs.paragraphs
            val arr : ArrayList<String> = arrayListOf()
            arr.addAll(paras)
            runOnUiThread{
                val intent = Intent(this@ListActivity, RuMediaActivity::class.java).apply {
                    putExtra("paragraphs", arr)
                }
                startActivity(intent)
            }
        }
    }


}