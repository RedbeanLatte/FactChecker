package com.redbeanlatte11.factchecker.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redbeanlatte11.factchecker.Event

class MoreViewModel : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }
}