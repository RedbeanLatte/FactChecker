package com.redbeanlatte11.factchecker.ui.setup

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetupViewModel : ViewModel() {

    private val _canLink = MutableLiveData<Boolean>()
    val canLink: LiveData<Boolean> = _canLink

    private val _reportMessage = MutableLiveData<String>()
    val reportMessage: LiveData<String> = _reportMessage

    private val _commentMessage = MutableLiveData<String>()
    val commentMessage: LiveData<String> = _commentMessage

    val autoCommentEnabled = MutableLiveData<Boolean>()

    val reportMessageTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {
            _reportMessage.value = p0.toString()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != null) {
                _canLink.value = !p0.isBlank()
            }
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != null) {
                _canLink.value = !p0.isBlank()
            }
        }
    }

    val commentMessageTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {
            _commentMessage.value = p0.toString()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    fun setReportMessage(reportMessage: String) {
        _reportMessage.value = reportMessage
    }

    fun setCommentMessage(commentMessage: String) {
        _commentMessage.value = commentMessage
    }

    fun setAutoCommentEnabled(autoCommentEnabled: Boolean) {
        this.autoCommentEnabled.value = autoCommentEnabled
    }
}