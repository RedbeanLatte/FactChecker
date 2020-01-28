package com.redbeanlatte11.factchecker.domain

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import timber.log.Timber

class SignInUseCase {

    @SuppressLint("SetJavaScriptEnabled")
    operator fun invoke(webView: WebView, onCompleted: () -> Unit) =
        with(webView) {
            settings.javaScriptEnabled = true
            webViewClient = GoogleAccountWebViewClient(onCompleted)
            loadUrl("https://accounts.google.com")
        }

    private class GoogleAccountWebViewClient(
        val onCompleted: () -> Unit
    ) : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Timber.d("onPageFinished, stage: $url")

            val savedSignInResult = PreferenceUtils.loadSignInResult(view.context)

            if (url.contains("https://accounts.google.com")) {
                if (savedSignInResult) {
                    PreferenceUtils.saveSignInResult(view.context, false)
                }

            } else if (url.contains("https://myaccount.google.com")) {
                if (!savedSignInResult) {
                    PreferenceUtils.saveSignInResult(view.context, true)
                }
                onCompleted()
            }
        }
    }
}