package com.redbeanlatte11.factchecker.ui.home

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.domain.LinkGoogleAccountUseCase
import kotlinx.coroutines.launch

class GoogleAccountViewModel(
    private val linkGoogleAccountUseCase: LinkGoogleAccountUseCase
) : ViewModel() {

    private val _signInCompletedEvent = MutableLiveData<Event<Unit>>()
    val signInCompletedEvent: LiveData<Event<Unit>> = _signInCompletedEvent

    private val _signOutCompletedEvent = MutableLiveData<Event<Unit>>()
    val signOutCompletedEvent: LiveData<Event<Unit>> = _signOutCompletedEvent

    fun linkGoogleAccount(webView: WebView) {
        viewModelScope.launch {
            linkGoogleAccountUseCase(
                webView,
                { _signInCompletedEvent.value = Event(Unit) },
                { _signOutCompletedEvent.value = Event(Unit) }
            )
        }
    }
}