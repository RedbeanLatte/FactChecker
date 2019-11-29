package com.redbeanlatte11.factchecker.ui.home

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import com.redbeanlatte11.factchecker.domain.SignInUseCase

class GoogleAccountViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    fun signIn(webView: WebView) {
        signInUseCase(webView)
    }
}