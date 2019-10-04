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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber

class VideosViewModel(
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

    private var reportAllJob: Job? = null

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [VideosDataSource]
     */
    fun loadVideos(forceUpdate: Boolean) {
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

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    fun reportAll(webView: WebView, reportMessage: String, onReportAllListener: OnReportAllListener) {
        reportAllJob = viewModelScope.launch {
            Timber.d("reportAll")
            items.value?.forEach { video ->
                yield()
                Timber.d("report video: ${video.snippet.title}")
                onReportAllListener.onNext(video)
                reportVideoUseCase(webView, "https://m.youtube.com/watch?v=${video.id}", reportMessage)
                //TODO: implement to add reported videos
            }
            Timber.d("reportAll completed")
            onReportAllListener.onCompleted()
            webView.loadUrl("https://m.youtube.com")
        }
    }

    fun cancelReportAll() {
        reportAllJob?.run {
            Timber.d("cancelReportAll")
            cancel("cancelReportAll")
        }
    }

    interface OnReportAllListener {

        fun onNext(video: Video)

        fun onCompleted()
    }
}