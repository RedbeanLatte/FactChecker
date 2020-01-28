package com.redbeanlatte11.factchecker.ui.popular

import androidx.lifecycle.*
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
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

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [VideosDataSource]
     */
    fun loadVideos(forceUpdate: Boolean) {
        _dataLoading.value = true

        viewModelScope.launch {
            val videosResult = getPopularVideosUseCase(forceUpdate)
            if (videosResult is Success) {
                isDataLoadingError.value = false
                _items.value = videosResult.data
            } else {
                Timber.e((videosResult as Error).exception)
                isDataLoadingError.value = false
                _items.value = emptyList()
                showSnackbarMessage(R.string.loading_videos_error)
            }

            _dataLoading.value = false
        }
    }

    fun refresh() {
        loadVideos(true)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }
}