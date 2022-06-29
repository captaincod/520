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
import com.example.a520.NewsResponse
import com.example.a520.dialogs.ConnectionDialog
import com.example.a520.R
import com.example.a520.activities.MainActivity.Companion.TAG
import com.example.a520.agents.RuAgent
import com.example.a520.agents.RuMedia
import com.example.a520.news.Dataset
import com.google.gson.Gson
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
import okhttp3.*
import okhttp3.Request
import java.io.IOException
import kotlin.collections.emptyList as emptyList

class ListActivity : AppCompatActivity() {

    lateinit var toMain: Button
    lateinit var progressBar: ProgressBar
    lateinit var getRuAgents: Button
    lateinit var getRuMedia: Button
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

        getRuMedia = findViewById(R.id.get_ru_media)
        getRuMedia.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            startActivity(Intent(this, RuMediaActivity::class.java))
        }

    }

    data class ScrapeData(var paragraphs: List<String> = emptyList())


    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}