package com.redbeanlatte11.factchecker.ui.home

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.domain.SignInUseCase
import kotlinx.coroutines.launch

class GoogleAccountViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _signInCompletedEvent = MutableLiveData<Event<Unit>>()
    val signInCompletedEvent: LiveData<Event<Unit>> = _signInCompletedEvent

    fun signIn(webView: WebView) {
        viewModelScope.launch {
            signInUseCase(webView) {
                _signInCompletedEvent.value = Event(Unit)
            }
        }
    }
}