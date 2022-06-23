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
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.get
import com.example.a520.*
import com.example.a520.recyclerview.RecyclerTouchListener.ClickListener
import com.example.a520.dialogs.ConnectionDialog
import com.example.a520.dialogs.LinkDialog
import com.example.a520.recyclerview.CustomRecyclerAdapter
import com.example.a520.recyclerview.RecyclerTouchListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    lateinit var toComparison: Button
    lateinit var ownComparison: Button
    lateinit var toList: Button
    //lateinit var rus: Button
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

        ownComparison = findViewById(R.id.own_comparison)
        showEditTextDialog()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                recyclerView,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val count: Int = recyclerView.childCount
                        var positionView = position
                        if (positionView >= count){
                            positionView -= count
                        }
                        if (recyclerView[positionView].tag != null){
                            recyclerView[positionView].background = getDrawable(R.drawable.bordered)
                            if (recyclerView[positionView].tag == "first"){
                                firstComparable = ""
                            } else if (recyclerView[positionView].tag == "second"){
                                secondComparable = ""
                            }
                            recyclerView[positionView].tag = null
                            toComparison.isClickable = false
                            toComparison.isEnabled = false
                            toComparison.setBackgroundColor(resources.getColor(R.color.light_gray))
                        } else {
                            if (firstComparable == ""){
                                recyclerView[positionView].background = getDrawable(R.drawable.bordered_select)
                                firstComparable = links[position]
                                recyclerView[positionView].tag = "first"
                            } else if (secondComparable == ""){
                                recyclerView[positionView].background = getDrawable(R.drawable.bordered_select)
                                secondComparable = links[position]
                                recyclerView[positionView].tag = "second"
                            }

                            if (firstComparable != "" && secondComparable != ""){
                                toComparison.isClickable = true
                                toComparison.isEnabled = true
                                toComparison.setBackgroundColor(resources.getColor(R.color.green))
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

    private fun showEditTextDialog() {
        ownComparison.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
            val editText1 = dialogLayout.findViewById<EditText>(R.id.edittext_1)
            val editText2 = dialogLayout.findViewById<EditText>(R.id.edittext_2)

            with(builder) {
                setIcon(R.drawable.world_news)
                setTitle("Введите источники для сравнения")
                setPositiveButton("ОК"){dialog, which ->
                    val intentComparison = Intent(this@MainActivity, ComparisonActivity::class.java).apply {
                        putExtra("first", editText1.text.toString())
                        putExtra("second", editText2.text.toString())
                    }
                    startActivity(intentComparison)
                }
                setNegativeButton("Отмена", null)
                setView(dialogLayout)
                show()
            }
        }
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
                    val newtitles: MutableList<String> = mutableListOf()
                    val newlinks: MutableList<String> = mutableListOf()
                    val newsources: MutableList<String>  = mutableListOf()
                    val newdates: MutableList<String>  = mutableListOf()
                    val newimages: MutableList<String>  = mutableListOf()
                    for (news in newsData.results){
                        newtitles.add(news.title)
                        newlinks.add(news.link)
                        newsources.add(news.source_id)
                        newdates.add(news.pubDate)
                        newimages.add(news.image_url.toString())

                    }
                    titles = newtitles
                    links = newlinks
                    sources = newsources
                    dates = newdates
                    images = newimages
                }
            })
            Log.d(DEVELOP, titles.toString())
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which){
            DialogInterface.BUTTON_POSITIVE -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkForDialog)))
        }
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

    @DelicateCoroutinesApi
    fun getRU(view: android.view.View) {
        GlobalScope.launch (Dispatchers.IO) {
            getResponse("спецоперация%20OR%20украина", "ru")
            runOnUiThread {
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = CustomRecyclerAdapter(applicationContext, titles, sources, dates, images)
            }
        }

    }

    @DelicateCoroutinesApi
    fun getUK(view: android.view.View) {
        GlobalScope.launch (Dispatchers.IO) {
            getResponse("війна%20OR%20росія", "uk")
            runOnUiThread {
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = CustomRecyclerAdapter(applicationContext, titles, sources, dates, images)
            }
        }

    }

    fun getEN(view: android.view.View) {}
}