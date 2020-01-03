package com.redbeanlatte11.factchecker.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.domain.GetPopularVideosUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class PopularViewModel(
    private val getPopularVideosUseCase: GetPopularVideosUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<Video>>().apply { value = emptyList() }
    val items: LiveData<List<Video>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val isDataLoadingError = MutableLiveData<Boolean>()

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [VideosDataSource]
     */
    fun loadVideos(forceUpdate: Boolean) {
        _dataLoading.value = true

        viewModelScope.launch {
            val videosResult = getPopularVideosUseCase(forceUpdate)
            if (videosResult is Result.Success) {
                isDataLoadingError.value = false
                _items.value = videosResult.data
            } else {
                Timber.e((videosResult as Result.Error).exception)
                isDataLoadingError.value = false
                _items.value = emptyList()
                showSnackbarMessage(R.string.loading_videos_error)
            }

            _dataLoading.value = false
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    //TODO: implement shareVideoUseCase
}