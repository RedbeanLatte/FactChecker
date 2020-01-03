package com.redbeanlatte11.factchecker.ui.home

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.domain.AddBlacklistVideoUseCase
import com.redbeanlatte11.factchecker.util.YoutubeUrlUtils
import kotlinx.coroutines.launch

class AddBlacklistVideoViewModel(
    private val addBlacklistVideoUseCase: AddBlacklistVideoUseCase
) : ViewModel() {

    private val _canAdd = MutableLiveData<Boolean>()
    val canAdd: LiveData<Boolean> = _canAdd

    private val _videoUrl = MutableLiveData<String>()
    val videoUrl: LiveData<String> = _videoUrl

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    val urlTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _canAdd.value = false
        }
    }

    val descriptionTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _description.value = p0.toString()
        }
    }

    fun setVideoUrl(videoUrl: String) {
        viewModelScope.launch {
            _videoUrl.value = videoUrl
        }
    }

    //TODO: add to check reduplication
    fun confirmVideoUrl(url: String) {
        viewModelScope.launch {
            if (YoutubeUrlUtils.validateUrl(url)) {
                _canAdd.value = true
                showSnackbarMessage(R.string.confirm_url_pass)
            } else {
                _canAdd.value = false
                showSnackbarMessage(R.string.confirm_url_fail)
            }
        }
    }

    fun addBlacklistVideo(url: String, description: String) {
        viewModelScope.launch {
            addBlacklistVideoUseCase(YoutubeUrlUtils.extractIdFromUrl(url)!!, description)
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }
}