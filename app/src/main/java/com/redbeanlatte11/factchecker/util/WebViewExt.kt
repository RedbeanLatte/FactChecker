package com.redbeanlatte11.factchecker.util

import android.webkit.WebView
import timber.log.Timber

fun WebView.loadYoutubeHome() {
    Timber.d("loadYoutubeHome")
    loadUrl("https://m.youtube.com")
}