package com.redbeanlatte11.factchecker.ui.home

import android.webkit.WebView
import androidx.lifecycle.*
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
import kotlinx.coroutines.*
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

    private var reportJob: Job? = null

    private var _currentFiltering = VideosFilterType.ALL_VIDEOS

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

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

    fun reportAll(
        webView: WebView,
        reportMessage: String,
        onReportAllListener: OnReportAllListener
    ) {
        var reportedVideoCount = 0

        reportAllJob = viewModelScope.launch {
            Timber.d("reportAll")
            items.value?.forEach { video ->
                Timber.d("report video: ${video.snippet.title}")
                onReportAllListener.onNext(video)
                try {
                    reportVideoUseCase(webView, video, reportMessage)
                    reportedVideoCount++
                } catch (e: TimeoutCancellationException) {
                    Timber.w("report video timed out")
                }
            }
            Timber.d("reportAll completed")
            items.value?.let { onReportAllListener.onCompleted(reportedVideoCount) }
            loadVideos(false)
        }
    }

    fun cancelReportAll() {
        reportAllJob?.run {
            Timber.d("cancelReportAll")
            cancel("cancelReportAll")
            loadVideos(false)
            showSnackbarMessage(R.string.cancel_report_all)
        }
    }

    fun reportVideo(
        webView: WebView,
        video: Video,
        reportMessage: String,
        onReportCompleteListener: OnReportCompleteListener
    ) {
        reportJob = viewModelScope.launch {
            Timber.d("addBlacklistVideo: ${video.snippet.title}")
            try {
                reportVideoUseCase(webView, video, reportMessage)
                onReportCompleteListener.onComplete(video)
                loadVideos(false)
            } catch (e: TimeoutCancellationException) {
                Timber.d("addBlacklistVideo timed out")
                showSnackbarMessage(R.string.time_out_message)
            }
        }
    }

    fun cancelReportVideo() {
        reportJob?.run {
            Timber.d("cancelReportVideo")
            cancel("cancelReportVideo")
            loadVideos(false)
            showSnackbarMessage(R.string.cancel_report)
        }
    }

    fun excludeVideo(video: Video, excluded: Boolean) = viewModelScope.launch {
        if (excluded) {
            excludeVideoUseCase(video)
            showSnackbarMessage(R.string.video_marked_excluded)
        } else {
            includeVideoUseCase(video)
            showSnackbarMessage(R.string.video_marked_added)
        }
        loadVideos(false)
    }

    fun refresh() {
        loadVideos(true)
    }
}