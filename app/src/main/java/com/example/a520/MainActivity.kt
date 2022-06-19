package com.example.a520

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.os.Handler
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var toComparison: Button
    lateinit var toList: Button
    lateinit var firstComparable: MutableMap<String, String>
    lateinit var secondComparable: MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isConnected()){
            ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
        }
        getResponse("спецоперация", "ru")

        firstComparable = mutableMapOf(
            "title" to "No title", "url" to "https://www.vedomosti.ru/politics/news/2022/06/16/926809-pushilin-spetsoperatsii", "image" to ""
        )
        secondComparable = mutableMapOf(
            "title" to "No title", "url" to "https://gazeta.ua/articles/sport/_polskij-bokser-adamek-zayaviv-scho-v-ukrayini-jde-gromadyanska-vijna/1095377", "image" to ""
        )

        textView = findViewById(R.id.text)
        toComparison = findViewById(R.id.to_comparison)

        toComparison.setOnClickListener{
            if (!isConnected()){
                ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
            } else {
                val intentComparison = Intent(this, ComparisonActivity::class.java).apply {
                    putExtra("first", firstComparable["url"])
                    putExtra("second", secondComparable["url"])
                }
                startActivity(intentComparison)
            }
        }

        toList = findViewById(R.id.to_list)
        toList.setOnClickListener{
            val intentList = Intent(this, ListActivity::class.java).apply {
                putExtra("physical", "https://minjust.gov.ru/ru/activity/directions/942/spisok-lic-vypolnyayushih-funkcii-inostrannogo-agenta/")
                putExtra("media", "https://minjust.gov.ru/ru/documents/7755/")
            }
            startActivity(intentList)
        }

    }

    fun getResponse(q: String, country: String){

        val client = OkHttpClient()
        val URL =
            "https://newsdata.io/api/1/news?apikey=${getString(R.string.API)}&country=$country&q=$q"
        val request: Request = Request.Builder()
            .url(URL)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Log.w("DEVELOP", "New call on failure: $e")
                }
            }
            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                val newsData: NewsResponse = Gson().fromJson(json, NewsResponse::class.java)
                Log.d(DEVELOP, newsData.toString())
                runOnUiThread {
                    textView.text = "dd"
                }
            }
        })
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    companion object {
        const val DEVELOP = "develop"
    }
}