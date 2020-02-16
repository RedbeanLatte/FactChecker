package com.redbeanlatte11.factchecker.util

import android.webkit.WebView
import timber.log.Timber

fun WebView.loadBlank() {
    loadUrl("about:blank")
}