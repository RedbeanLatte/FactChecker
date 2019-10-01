package com.redbeanlatte11.factchecker.home

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.domain.GetVideosUseCase
import com.redbeanlatte11.factchecker.domain.ReportVideoUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val getVideosUseCase: GetVideosUseCase,
    private val reportVideoUseCase: ReportVideoUseCase
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
    private fun loadVideos(forceUpdate: Boolean, needsCheck: Boolean) {
        _dataLoading.value = true

        viewModelScope.launch {
            val videosResult = getVideosUseCase(forceUpdate)
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

    fun loadVideos(forceUpdate: Boolean) {
        loadVideos(forceUpdate, false)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    //TODO: implement reportAll
    fun reportAll(webView: WebView) {
        viewModelScope.launch {
            val reportMessage = "This channel makes misrepresentative contents"

            reportVideoUseCase(webView, "https://www.youtube.com/watch?v=E7s4R7T-N40", reportMessage)
            reportVideoUseCase(webView, "https://www.youtube.com/watch?v=KFWmvEPC3XI", reportMessage)
            webView.loadUrl("https://www.youtube.com")
        }
    }
}