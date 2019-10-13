package com.redbeanlatte11.factchecker.home

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.domain.AddVideoBlacklistUseCase
import com.redbeanlatte11.factchecker.domain.ConfirmVideoUrlUseCase
import kotlinx.coroutines.launch

class AddVideoBlacklistViewModel(
    private val confirmVideoUrlUseCase: ConfirmVideoUrlUseCase,
    private val addVideoBlacklistUseCase: AddVideoBlacklistUseCase
) : ViewModel() {

    private val _canAdd = MutableLiveData<Boolean>()
    val canAdd: LiveData<Boolean> = _canAdd

    private val _videoUrl = MutableLiveData<String>()
    val videoUrl: LiveData<String> = _videoUrl

    val urlTextWatcher = object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _canAdd.value = false
        }
    }

    fun setVideoUrl(videoUrl: String) {
        viewModelScope.launch {
            _videoUrl.value = videoUrl
        }
    }

    fun confirmVideoUrl(url: String) {
        viewModelScope.launch {
            val resultVideo = confirmVideoUrlUseCase(url)

            if (resultVideo is Success) {
                _canAdd.value = true
            }

            if (resultVideo is Error) {
                _canAdd.value = true
            }
        }
    }

    fun addVideoBlacklist(url: String, description: String) {
        addVideoBlacklistUseCase(url, description)
    }
}