package com.redbeanlatte11.factchecker.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.domain.AddBlacklistChannelUseCase
import com.redbeanlatte11.factchecker.domain.AddBlacklistVideoUseCase
import com.redbeanlatte11.factchecker.domain.GetChannelUseCase
import com.redbeanlatte11.factchecker.domain.GetVideoUseCase
import com.redbeanlatte11.factchecker.util.YoutubeUrlUtils
import kotlinx.coroutines.launch
import timber.log.Timber

class ShareViewModel(
    private val addBlacklistVideoUseCase: AddBlacklistVideoUseCase,
    private val addBlacklistChannelUseCase: AddBlacklistChannelUseCase,
    private val getVideoUseCase: GetVideoUseCase,
    private val getChannelUseCase: GetChannelUseCase
) : ViewModel() {

    private val _videoUrlConfirmedEvent = MutableLiveData<Event<Unit>>()
    val videoUrlConfirmedEvent: LiveData<Event<Unit>> = _videoUrlConfirmedEvent

    private val _channelUrlConfirmedEvent = MutableLiveData<Event<Unit>>()
    val channelUrlConfirmedEvent: LiveData<Event<Unit>> = _channelUrlConfirmedEvent

    private val _duplicatedUrlEvent = MutableLiveData<Event<Unit>>()
    val duplicatedUrlEvent: LiveData<Event<Unit>> = _duplicatedUrlEvent

    private val _blacklistAddedEvent = MutableLiveData<Event<Unit>>()
    val blacklistAddedEvent: LiveData<Event<Unit>> = _blacklistAddedEvent

    private val _addFailedEvent = MutableLiveData<Event<Unit>>()
    val addFailedEvent: LiveData<Event<Unit>> = _addFailedEvent

    fun confirmUrl(url: String) {
        viewModelScope.launch {
            if (YoutubeUrlUtils.validateVideoUrl(url)) {
                val videoId = YoutubeUrlUtils.extractVideoIdFromUrl(url)
                val foundVideo = getVideoUseCase(videoId!!, true)
                if (foundVideo is Success) {
                    _duplicatedUrlEvent.value = Event(Unit)
                } else {
                    _videoUrlConfirmedEvent.value = Event(Unit)
                }
            } else if (YoutubeUrlUtils.validateChannelUrl(url)) {
                val channelId = YoutubeUrlUtils.extractChannelIdFromUrl(url)
                if (channelId != null) {
                    val foundChannel = getChannelUseCase(channelId, true)
                    if (foundChannel is Success) {
                        _duplicatedUrlEvent.value = Event(Unit)
                    } else {
                        _channelUrlConfirmedEvent.value = Event(Unit)
                    }
                } else { // userName case
                    _channelUrlConfirmedEvent.value = Event(Unit)
                }
            } else {
                _addFailedEvent.value = Event(Unit)
            }
        }
    }

    fun addBlacklistVideo(url: String) {
        viewModelScope.launch {
            val result = addBlacklistVideoUseCase(url)
            if (result is Success) {
                _blacklistAddedEvent.value = Event(Unit)
            } else {
                Timber.e((result as Error).exception)
                _addFailedEvent.value = Event(Unit)
            }
        }
    }

    fun addBlacklistChannel(url: String) {
        viewModelScope.launch {
            val result = addBlacklistChannelUseCase(url)
            if (result is Success) {
                _blacklistAddedEvent.value = Event(Unit)
            } else {
                Timber.e((result as Error).exception)
                _addFailedEvent.value = Event(Unit)
            }
        }
    }
}