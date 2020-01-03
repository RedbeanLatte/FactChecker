package com.redbeanlatte11.factchecker.ui.channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.source.ChannelsDataSource
import com.redbeanlatte11.factchecker.domain.GetChannelsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class ChannelsViewModel(
    private val getChannelsUseCase: GetChannelsUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<Channel>>().apply { value = emptyList() }
    val items: LiveData<List<Channel>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val isDataLoadingError = MutableLiveData<Boolean>()

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [ChannelsDataSource]
     */
    private fun loadChannels(forceUpdate: Boolean, needsCheck: Boolean) {
        _dataLoading.value = true

        viewModelScope.launch {
            val channelsResult = getChannelsUseCase(forceUpdate)
            if (channelsResult is Result.Success) {
                isDataLoadingError.value = false
                _items.value = channelsResult.data
            } else {
                Timber.e((channelsResult as Result.Error).exception)
                isDataLoadingError.value = false
                _items.value = emptyList()
                showSnackbarMessage(R.string.loading_channels_error)
            }

            _dataLoading.value = false
        }
    }

    fun loadChannels(forceUpdate: Boolean) {
        loadChannels(forceUpdate, false)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    //TODO: implement shareVideoUseCase
}