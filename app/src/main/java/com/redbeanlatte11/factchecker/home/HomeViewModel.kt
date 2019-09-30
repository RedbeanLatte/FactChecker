package com.redbeanlatte11.factchecker.home

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.domain.ReportVideoUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val reportVideoUseCase: ReportVideoUseCase
) : ViewModel() {

    fun reportAll(webView: WebView) {
        viewModelScope.launch {
            val reportMessage = "This channel makes misrepresentative contents"

            reportVideoUseCase(webView, "https://www.youtube.com/watch?v=E7s4R7T-N40", reportMessage)
            reportVideoUseCase(webView, "https://www.youtube.com/watch?v=KFWmvEPC3XI", reportMessage)
            webView.loadUrl("https://www.youtube.com")
        }
    }
}