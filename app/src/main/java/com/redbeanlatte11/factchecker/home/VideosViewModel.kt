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
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import com.redbeanlatte11.factchecker.domain.ExcludeVideoUseCase
import com.redbeanlatte11.factchecker.domain.GetVideosUseCase
import com.redbeanlatte11.factchecker.domain.IncludeVideoUseCase
import com.redbeanlatte11.factchecker.domain.ReportVideoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber

class VideosViewModel(
    private val getVideosUseCase: GetVideosUseCase,
    private val reportVideoUseCase: ReportVideoUseCase,
    private val excludeVideoUseCase: ExcludeVideoUseCase,
    private val includeVideoUseCase: IncludeVideoUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<Video>>().apply { value = emptyList() }
    val items: LiveData<List<Video>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var reportAllJob: Job? = null

    private var _currentFiltering = VideosFilterType.ALL_VIDEOS

    fun setFiltering(requestType: VideosFilterType) {
        _currentFiltering = requestType
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [VideosDataSource]
     */
    fun loadVideos(forceUpdate: Boolean) {
        _dataLoading.value = true

        viewModelScope.launch {
            val videosResult = getVideosUseCase(forceUpdate, _currentFiltering)
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
                reportVideoUseCase(webView, video, reportMessage)
            }
            Timber.d("reportAll completed")
            onReportAllListener.onCompleted()
            webView.loadUrl("https://m.youtube.com")
            loadVideos(false)
        }
    }

    fun cancelReportAll() {
        reportAllJob?.run {
            Timber.d("cancelReportAll")
            cancel("cancelReportAll")
        }
    }

    fun excludeVideo(video: Video, excluded: Boolean)  = viewModelScope.launch {
        if (excluded) {
            excludeVideoUseCase(video)
            showSnackbarMessage(R.string.video_marked_excluded)
        } else {
            includeVideoUseCase(video)
            showSnackbarMessage(R.string.video_marked_added)
        }
        loadVideos(false)
    }

    interface OnReportAllListener {

        fun onNext(video: Video)

        fun onCompleted()
    }
}