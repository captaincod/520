package com.example.a520.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

import android.net.ConnectivityManager
import android.net.Uri
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.*
import com.example.a520.dialogs.ConnectionDialog
import com.example.a520.dialogs.LinkDialog
import com.example.a520.recyclerview.CustomRecyclerAdapter
import com.example.a520.recyclerview.Dataset


class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    lateinit var toComparison: Button
    lateinit var ownComparison: Button
    lateinit var toList: Button
    lateinit var recyclerView: RecyclerView

    var linkForDialog: String = ""
    var alreadySelected: MutableList<String> = mutableListOf()
    lateinit var ruData: LanguageData
    lateinit var ukData: LanguageData
    lateinit var enData: LanguageData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isConnected()){
            ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
        }
        ruData = LanguageData(getResponse("спецоперация%20OR%20украина", "ru"), itemSelectedList = mutableListOf())
        ukData = LanguageData(getResponse("війна%20OR%20росія", "uk"), itemSelectedList = mutableListOf())
        enData = LanguageData(getResponse("war%20OR%20ukraine", "en"), itemSelectedList = mutableListOf())

        toComparison = findViewById(R.id.to_comparison)
        comparisonAction("off")
        toComparison.setOnClickListener{
            if (!isConnected()){
                ConnectionDialog().show(supportFragmentManager, "EmptyDialog")
            } else {
                val intentComparison = Intent(this, ComparisonActivity::class.java).apply {
                    putExtra("first", alreadySelected[0])
                    putExtra("second", alreadySelected[1])
                }
                startActivity(intentComparison)
            }
        }

        toList = findViewById(R.id.to_list)
        toList.setOnClickListener{
            startActivity(Intent(this, ListActivity::class.java))
        }

        ownComparison = findViewById(R.id.own_comparison)
        showEditTextDialog()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
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

    private fun getResponse(q: String, lang: String): MutableList<Dataset> {
        val datasetList: MutableList<Dataset> = mutableListOf()
        val client = OkHttpClient()
        val URL =
            "https://newsdata.io/api/1/news?apikey=${getString(R.string.API)}&language=$lang&q=$q"
        val request: Request = Request.Builder()
            .url(URL)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Log.w(TAG, "New call on failure: $e")
                }
            }
            override fun onResponse(call: Call?, response: Response?) {
                Log.d(TAG, "Get Response $lang")
                val json = response?.body()?.string()
                val newsData = Gson().fromJson(json, NewsResponse::class.java)
                for (news in newsData.results){
                    if (news.source_id.length > 7){
                        news.source_id = news.source_id.substring(0,7) + "..."
                    }
                    datasetList.add(Dataset(
                        news.title, news.link, news.source_id, news.pubDate, news.image_url.toString(), false)
                    )
                }
            }
        })
        return datasetList
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
        const val TAG = "develop"
    }

    private fun comparisonAction(action: String){
        when (action) {
            "on" -> {
                toComparison.isClickable = true
                toComparison.isEnabled = true
                toComparison.setBackgroundColor(resources.getColor(R.color.green))
            }
            "off" -> {
                toComparison.isClickable = false
                toComparison.isEnabled = false
                toComparison.setBackgroundColor(resources.getColor(R.color.light_gray_tint))
            }
            else -> {
                linkForDialog = action
                LinkDialog(linkForDialog).show(supportFragmentManager, "YesNoDialog")
            }
        }
    }

    fun getRU(view: android.view.View) {
        recyclerView.adapter =
            CustomRecyclerAdapter(applicationContext, ruData.dataset, ruData.itemSelectedList, alreadySelected){
                    action -> comparisonAction(action) }
    }


    fun getUK(view: android.view.View) {
        recyclerView.adapter =
            CustomRecyclerAdapter(applicationContext, ukData.dataset, ukData.itemSelectedList, alreadySelected){
                    action -> comparisonAction(action) }
    }

    fun getEN(view: android.view.View) {
        recyclerView.adapter =
            CustomRecyclerAdapter(applicationContext, enData.dataset, enData.itemSelectedList, alreadySelected){
                    action -> comparisonAction(action) }
    }

    data class LanguageData(var dataset: MutableList<Dataset>, var itemSelectedList: MutableList<Int>)
}