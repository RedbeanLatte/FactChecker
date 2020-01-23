package com.redbeanlatte11.factchecker.ui.home

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.domain.SignInUseCase
import kotlinx.coroutines.launch

class GoogleAccountViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    fun signIn(webView: WebView) {
        viewModelScope.launch {
            signInUseCase(webView)
        }
    }
}