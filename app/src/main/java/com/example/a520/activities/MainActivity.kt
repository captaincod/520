package com.example.a520.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.get
import com.example.a520.*
import com.example.a520.recyclerview.RecyclerTouchListener.ClickListener
import com.example.a520.dialogs.ConnectionDialog
import com.example.a520.dialogs.LinkDialog
import com.example.a520.recyclerview.CustomRecyclerAdapter
import com.example.a520.recyclerview.RecyclerTouchListener


class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    lateinit var toComparison: Button
    lateinit var toList: Button
    lateinit var rus: Button
    lateinit var recyclerView: RecyclerView

    var firstComparable: String = ""
    var secondComparable: String = ""
    var titles: MutableList<String> = mutableListOf()
    var links: MutableList<String> = mutableListOf()
    var sources: MutableList<String>  = mutableListOf()
    var dates: MutableList<String>  = mutableListOf()
    var images: MutableList<String>  = mutableListOf()
    var linkForDialog: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toComparison = findViewById(R.id.to_comparison)
        toComparison.isClickable = false
        toComparison.isEnabled = false
        toComparison.setBackgroundColor(Color.LTGRAY)
        toComparison.setOnClickListener{
            if (!isConnected()){
                ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
            } else {
                val intentComparison = Intent(this, ComparisonActivity::class.java).apply {
                    putExtra("first", firstComparable)
                    putExtra("second", secondComparable)
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

        getResponse("спецоперация%20OR%20украина", "ru")

        recyclerView = findViewById(R.id.recyclerView)
        rus = findViewById(R.id.rus)
        rus.setOnClickListener{
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CustomRecyclerAdapter(applicationContext, titles, sources, dates, images)
        }

        recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                recyclerView,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val count: Int = recyclerView.layoutManager!!.childCount
                        var positionView = position
                        if (positionView >= count){
                            positionView -= count
                        }
                        if (recyclerView[positionView].tag != null){
                            recyclerView[positionView].setBackgroundColor(Color.TRANSPARENT)
                            if (recyclerView[positionView].tag == "first"){
                                firstComparable = ""
                            } else if (recyclerView[positionView].tag == "second"){
                                secondComparable = ""
                            }
                            recyclerView[positionView].tag = null
                            toComparison.isClickable = false
                            toComparison.isEnabled = false
                            toComparison.setBackgroundColor(Color.LTGRAY)
                        } else {
                            if (firstComparable == ""){
                                recyclerView[positionView].setBackgroundColor(Color.LTGRAY)
                                firstComparable = links[position]
                                recyclerView[positionView].tag = "first"
                            } else if (secondComparable == ""){
                                recyclerView[positionView].setBackgroundColor(Color.LTGRAY)
                                secondComparable = links[position]
                                recyclerView[positionView].tag = "second"
                            }

                            if (firstComparable != "" && secondComparable != ""){
                                toComparison.isClickable = true
                                toComparison.isEnabled = true
                                toComparison.setBackgroundColor(Color.GREEN)
                            }
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {
                        linkForDialog = links[position]
                        LinkDialog(linkForDialog).show(supportFragmentManager, "YesNoDialog")
                    }
                })
        )
    }

    private fun getResponse(q: String, lang: String){
        if (!isConnected()){
            ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
        } else {
            val client = OkHttpClient()
            val URL =
                "https://newsdata.io/api/1/news?apikey=${getString(R.string.API)}&language=$lang&q=$q"
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
                    Log.d(DEVELOP, "Get Response")
                    val json = response?.body()?.string()
                    val newsData = Gson().fromJson(json, NewsResponse::class.java)
                    //Log.d(DEVELOP, newsData.totalResults.toString())
                    for (news in newsData.results){
                        //titles.add("<a href=\"${news.link}\">${news.title}</a>")
                        titles.add(news.title)
                        links.add(news.link)
                        sources.add(news.source_id)
                        dates.add(news.pubDate)
                        images.add(news.image_url.toString())
                    }
                }
            })
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which){
            DialogInterface.BUTTON_POSITIVE -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkForDialog)))
        }
    }

    fun enableButton(){

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