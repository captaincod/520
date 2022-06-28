package com.example.a520.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.webkit.WebViewClient
import com.example.a520.R
import android.webkit.WebResourceError

import android.webkit.WebResourceRequest
import android.widget.TextView
import com.example.a520.activities.MainActivity.Companion.TAG


class ComparisonActivity : AppCompatActivity() {

    lateinit var back: Button
    lateinit var firstWebView: WebView
    lateinit var secondWebView: WebView
    lateinit var firstError: TextView
    lateinit var secondError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparison)

        val firstURL = intent.getStringExtra("first")
        val secondURL = intent.getStringExtra("second")

        back = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        firstError = findViewById(R.id.first_web_error)
        firstWebView = findViewById(R.id.first_web)
        firstWebView.webViewClient = CustomWebViewClient(firstError)
        if (firstURL != null) {
            firstWebView.loadUrl(firstURL)
        }

        secondError = findViewById(R.id.second_web_error)
        secondWebView = findViewById(R.id.second_web)
        secondWebView.webViewClient = CustomWebViewClient(secondError)
        if (secondURL != null) {
            secondWebView.loadUrl(secondURL)
        }
    }

    private class CustomWebViewClient(var errorView: TextView) : WebViewClient() {
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            Log.d(TAG, "$view: ${error.description}")
            view.visibility = View.INVISIBLE
            errorView.visibility = View.VISIBLE
        }
    }
}