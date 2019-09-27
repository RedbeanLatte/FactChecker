package com.redbeanlatte11.factchecker.home

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.domain.ReportVideoUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val reportVideoUseCase: ReportVideoUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun reportVideo(webView: WebView, url: String) {
        reportVideoUseCase(webView , url, "This channel makes misrepresentative contents")
    }
}