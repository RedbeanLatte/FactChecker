package com.redbeanlatte11.factchecker.ui.setup

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redbeanlatte11.factchecker.Event

class SetupViewModel : ViewModel() {

    private val _canLink = MutableLiveData<Boolean>(false)
    val canLink: LiveData<Boolean> = _canLink

    private var _reportMessage = MutableLiveData<String>()
    val reportMessage: LiveData<String> = _reportMessage

    private val _setupParamsSavedEvent = MutableLiveData<Event<SetupParams>>()
    val setupParamsSavedEvent: LiveData<Event<SetupParams>> = _setupParamsSavedEvent

    private val _linkToGoogleAccountEvent = MutableLiveData<Event<Unit>>()
    val linkToGoogleAccountEvent: LiveData<Event<Unit>> = _linkToGoogleAccountEvent

    val reportMessageTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _reportMessage.value = p0.toString()
            if (p0 != null) {
                _canLink.value = !p0.isBlank()
            }
        }
    }

    fun linkToGoogleAccount() {
        val setupParams = SetupParams(_reportMessage.value ?: "")
        _setupParamsSavedEvent.value = Event(setupParams)
        _linkToGoogleAccountEvent.value = Event(Unit)
    }

    fun setReportMessage(reportMessage: String) {
        _reportMessage.value = reportMessage
        if (reportMessage.isNotEmpty()) {
            _canLink.value = true
        }
    }
}