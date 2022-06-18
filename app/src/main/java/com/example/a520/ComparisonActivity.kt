package com.example.a520

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.webkit.WebViewClient




class ComparisonActivity : AppCompatActivity() {

    lateinit var back: Button
    lateinit var firstWebView: WebView
    lateinit var secondWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparison)

        val firstURL = intent.getStringExtra("first")
        val secondURL = intent.getStringExtra("second")

        back = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        firstWebView = findViewById(R.id.first_web)
        firstWebView.webViewClient = WebViewClient()
        if (firstURL != null) {
            firstWebView.loadUrl(firstURL)
        }


        secondWebView = findViewById(R.id.second_web)
        secondWebView.webViewClient = WebViewClient()
        if (secondURL != null) {
            secondWebView.loadUrl(secondURL)
        }
    }
}