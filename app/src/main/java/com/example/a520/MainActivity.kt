package com.example.a520

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var toComparison: Button
    lateinit var toList: Button
    lateinit var firstComparable: MutableMap<String, String>
    lateinit var secondComparable: MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstComparable = mutableMapOf(
            "title" to "No title", "url" to "https://www.vedomosti.ru/politics/news/2022/06/16/926809-pushilin-spetsoperatsii", "image" to ""
        )
        secondComparable = mutableMapOf(
            "title" to "No title", "url" to "https://gazeta.ua/articles/sport/_polskij-bokser-adamek-zayaviv-scho-v-ukrayini-jde-gromadyanska-vijna/1095377", "image" to ""
        )

        textView = findViewById(R.id.text)
        toComparison = findViewById(R.id.to_comparison)

        toComparison.setOnClickListener{
            val intentComparison = Intent(this, ComparisonActivity::class.java).apply {
                putExtra("first", firstComparable["url"])
                putExtra("second", secondComparable["url"])
            }
            startActivity(intentComparison)
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

    fun getResponse(q: String, lang: String){
        val client = OkHttpClient()
        val URL =
            "https://newscatcher.p.rapidapi.com/v1/search_free?q=$q&lang=$lang&media=True"

        val request: Request = Request.Builder()
            .url(URL)
            .addHeader("X-RapidAPI-Key", "c9c0365ba4mshb68568657356927p16a43djsn95e258a13f17")
            .addHeader("X-RapidAPI-Host", "newscatcher.p.rapidapi.com")
            .get()
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Log.w("DEVELOP", "New call on failure: $e")
                }
            }
            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                val newsData: NewsResponse = Gson().fromJson<NewsResponse>(json, NewsResponse::class.java)
                Log.d("DEVELOP", newsData.toString())
                Log.d("DEVELOP", newsData.articles[0].toString())
                runOnUiThread {
                    textView.text = newsData.articles[0].title
                }
            }
        })
    }

    companion object {
        const val DEVELOP = "develop"
    }
}