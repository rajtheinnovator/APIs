package com.enpassio.apis

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class WebViewClickActivity : AppCompatActivity() {

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_click)
        val view: WebView = findViewById(R.id.web_view_for_click)
        val url = "http://mentor-dashboard.udacity.com/reviews/overview"


        view.webViewClient = MytWebViewClient(url)
        view.settings.javaScriptEnabled = true
        view.settings.setDomStorageEnabled(true);
        view.postDelayed({ view.loadUrl(url) }, 500)


//        view.setWebViewClient(object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                view.loadUrl("javascript:document.getElementById('index--primary--P14pO index--_btn--9nYKH  index--small--ACszC').click()");
//            }
//        })
    }

    // SSL Error Tolerant Web View Client
    private inner class MytWebViewClient(val urls: String?) : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            Log.d("my_tag", "ssl error")
            handler.proceed() // Ignore SSL certificate errors
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, urls)
        }

        override fun onPageFinished(view: WebView?, url: String?) {

            Log.d("my_tag", "onPageLoadFinished")
            view?.loadUrl("javascript:document.getElementsByTagName.('button')['index--_btn--9nYKH'].click()");
        }
    }
}
