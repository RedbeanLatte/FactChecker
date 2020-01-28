package com.redbeanlatte11.factchecker.ui.channel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.domain.AddBlacklistChannelUseCase
import com.redbeanlatte11.factchecker.domain.GetChannelUseCase
import com.redbeanlatte11.factchecker.util.YoutubeUrlUtils
import kotlinx.coroutines.launch
import timber.log.Timber

class AddBlacklistChannelViewModel(
    private val addBlacklistChannelUseCase: AddBlacklistChannelUseCase,
    private val getChannelUseCase: GetChannelUseCase
) : ViewModel() {

    private val _canAdd = MutableLiveData<Boolean>()
    val canAdd: LiveData<Boolean> = _canAdd

    private val _channelUrl = MutableLiveData<String>("")
    val channelUrl: LiveData<String> = _channelUrl

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _blacklistAddedEvent = MutableLiveData<Event<Unit>>()
    val blacklistAddedEvent: LiveData<Event<Unit>> = _blacklistAddedEvent

    val urlTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _canAdd.value = false
            _channelUrl.value = p0.toString()
        }
    }

    val descriptionTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _description.value = p0.toString()
        }
    }

    fun confirmChannelUrl(url: String) {
        viewModelScope.launch {
            if (YoutubeUrlUtils.validateChannelUrl(url)) {
                val channelId = YoutubeUrlUtils.extractChannelIdFromUrl(url)
                if (channelId != null) {
                    val foundChannel = getChannelUseCase(channelId, true)
                    if (foundChannel is Success) {
                        _canAdd.value = false
                        showSnackbarMessage(R.string.confirm_url_already_registered)
                    } else {
                        _canAdd.value = true
                        showSnackbarMessage(R.string.confirm_url_pass)
                    }
                } else { // userName case
                    _canAdd.value = true
                    showSnackbarMessage(R.string.confirm_url_pass)
                }
            } else {
                _canAdd.value = false
                showSnackbarMessage(R.string.confirm_url_fail)
            }
        }
    }

    fun addBlacklistChannel(url: String, description: String) {
        viewModelScope.launch {
            val result = addBlacklistChannelUseCase(url, description)
            if (result is Success) {
                showSnackbarMessage(R.string.adding_blacklist_success)
                _blacklistAddedEvent.value = Event(Unit)
            } else {
                Timber.e((result as Error).exception)
                showSnackbarMessage(R.string.adding_blacklist_error)
            }
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }
}