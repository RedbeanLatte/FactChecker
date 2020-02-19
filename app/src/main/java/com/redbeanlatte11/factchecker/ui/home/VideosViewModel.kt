package com.redbeanlatte11.factchecker.ui.home

import android.webkit.WebView
import androidx.lifecycle.*
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.ReportParams
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import com.redbeanlatte11.factchecker.domain.*
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

    private val _reportStartedEvent = MutableLiveData<Event<Int>>()
    val reportStartedEvent: LiveData<Event<Int>> = _reportStartedEvent

    private val _reportOnNextEvent = MutableLiveData<Event<Pair<Video, Int>>>()
    val reportOnNextEvent: LiveData<Event<Pair<Video, Int>>> = _reportOnNextEvent

    private val _reportCompletedEvent = MutableLiveData<Event<Int>>()
    val reportCompletedEvent: LiveData<Event<Int>> = _reportCompletedEvent

    private val _tooManyFlagsEvent = MutableLiveData<Event<Int>>()
    val tooManyFlagsEvent: LiveData<Event<Int>> = _tooManyFlagsEvent

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var reportJob: Job? = null

    private var _currentFiltering = VideosFilterType.ALL_VIDEOS

    private var _currentSearchPeriod = SearchPeriod.ALL

    private var reportedVideoCount = 0

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    fun setFiltering(requestType: VideosFilterType) {
        _currentFiltering = requestType
    }

    fun setSearchPeriod(searchPeriod: SearchPeriod) {
        _currentSearchPeriod = searchPeriod
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [VideosDataSource]
     */
    fun loadVideos(forceUpdate: Boolean) {
        _dataLoading.value = true

        viewModelScope.launch {
            val videosResult = getVideosUseCase(forceUpdate, _currentFiltering, _currentSearchPeriod)
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

    fun reportVideo(
        webView: WebView,
        reportParams: ReportParams,
        video: Video
    ) {
        reportVideos(webView, reportParams, listOf(video))
    }

    fun reportAllVideos(
        webView: WebView,
        reportParams: ReportParams
    ) {
        _items.value?.let { videos ->
            val videosToReport = if (videos.size > reportParams.targetCount) {
                videos.subList(0, reportParams.targetCount)
            } else {
                videos
            }

            reportVideos(webView, reportParams, videosToReport)
        }
    }

    private fun reportVideos(
        webView: WebView,
        reportParams: ReportParams,
        videoItems: List<Video>
    ) {
        reportJob = viewModelScope.launch {
            _reportStartedEvent.value = Event(videoItems.size)

            reportedVideoCount = 0
            videoItems.forEach { video ->
                if (!isActive) {
                    return@launch
                }

                Timber.d("report video: ${video.snippet.title}")
                _reportOnNextEvent.value = Event(Pair(video, reportedVideoCount))
                try {
                    reportVideoUseCase(webView, video, reportParams) {
                        Timber.w("Too many flags")
                        cancel(CancellationException("Too many flags"))
                        _tooManyFlagsEvent.value = Event(reportedVideoCount)
                        loadVideos(false)
                    }
                    reportedVideoCount += 1
                } catch (ex: TimeoutCancellationException) {
                    Timber.w("report video timed out")
                    showSnackbarMessage(R.string.time_out_message)
                }
            }

            if (isActive) {
                Timber.d("reportVideos completed")
                _reportCompletedEvent.value = Event(reportedVideoCount)
                loadVideos(false)
            }
        }
    }

    fun cancelReport() {
        reportJob?.run {
            if (isActive) {
                Timber.d("cancelReport")
                cancel("cancelReport")
                _reportCompletedEvent.value = Event(reportedVideoCount)
                loadVideos(false)
                showSnackbarMessage(R.string.cancel_report)
            }
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