package com.redbeanlatte11.factchecker.home

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import com.redbeanlatte11.factchecker.domain.SignInUseCase

class GoogleAccountViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    fun login(webView: WebView) {
        signInUseCase(webView)
    }
}