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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    lateinit var toComparison: Button
    lateinit var ownComparison: Button
    lateinit var toList: Button
    lateinit var recyclerView: RecyclerView

    var firstComparable: String = ""
    var secondComparable: String = ""
    var linkForDialog: String = ""
    var dataset: MutableList<Dataset> = mutableListOf()

    // TODO: убрать корутин, сделать три отдельных списка под каждую кнопку,
    // чтобы можно было выбирать на одной кнопке, перейти на вторую и вернуться на первую

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toComparison = findViewById(R.id.to_comparison)
        comparisonAction("off")
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
            startActivity(Intent(this, ListActivity::class.java))
        }

        ownComparison = findViewById(R.id.own_comparison)
        showEditTextDialog()

        recyclerView = findViewById(R.id.recyclerView)

    }

    private fun comparisonAction(action: String){
        if (action == "on"){
            toComparison.isClickable = true
            toComparison.isEnabled = true
            toComparison.setBackgroundColor(resources.getColor(R.color.green))
        }
        else if (action == "off"){
            toComparison.isClickable = false
            toComparison.isEnabled = false
            toComparison.setBackgroundColor(resources.getColor(R.color.light_gray))
        }
        else {
            linkForDialog = action
            LinkDialog(linkForDialog).show(supportFragmentManager, "YesNoDialog")
        }
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
                    val newdataset: MutableList<Dataset> = mutableListOf()
                    for (news in newsData.results){
                        newdataset.add(Dataset(
                            news.title, news.link, news.source_id, news.pubDate, news.image_url.toString(), false)
                        )
                    }
                    dataset = newdataset
                }
            })
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
                recyclerView.adapter = CustomRecyclerAdapter(applicationContext, dataset){
                    action -> comparisonAction(action)
                }
            }
        }

    }

    @DelicateCoroutinesApi
    fun getUK(view: android.view.View) {
        GlobalScope.launch (Dispatchers.IO) {
            getResponse("війна%20OR%20росія", "uk")
            runOnUiThread {
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = CustomRecyclerAdapter(applicationContext, dataset){
                        action -> comparisonAction(action)
                }
            }
        }

    }

    fun getEN(view: android.view.View) {}
}