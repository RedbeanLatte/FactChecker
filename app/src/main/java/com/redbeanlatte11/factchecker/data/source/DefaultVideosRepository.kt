package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.local.VideosLocalDataSource
import com.redbeanlatte11.factchecker.data.source.remote.VideosRemoteDataSource
import com.redbeanlatte11.factchecker.util.YoutubeUrlUtils
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DefaultVideosRepository(
    private val videosRemoteDataSource: VideosRemoteDataSource,
    private val videosLocalDataSource: VideosLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosRepository {

    private var cachedVideos: ConcurrentMap<String, Video>? = null

    override suspend fun getVideos(
        forceUpdate: Boolean,
        sortType: Video.SortType
    ): Result<List<Video>> {

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {
                cachedVideos?.let { cachedVideos ->
                    return@withContext Success(
                        cachedVideos.values.sortedWith(
                            Video.getComparator(
                                sortType
                            )
                        )
                    )
                }
            }

            val newVideos = fetchVideosFromRemoteOrLocal(forceUpdate)

            // Refresh the cache with the new videos
            (newVideos as? Success)?.let { refreshCache(it.data) }

            cachedVideos?.let { videos ->
                return@withContext Success(videos.values.sortedWith(Video.getComparator(sortType)))
            }

            (newVideos as? Success)?.let {
                if (it.data.isEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(IllegalStateException("Illegal state"))
        }
    }

    private suspend fun fetchVideosFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Video>> {
        // Remote first
        when (val remoteVideos = videosRemoteDataSource.getVideos()) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                val newVideos = combineLocalDataWithRemoteData(remoteVideos.data)
                refreshLocalDataSource(newVideos)
                return Success(newVideos)
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localVideos = videosLocalDataSource.getVideos()
        if (localVideos is Success) return localVideos
        return Error(Exception("Error fetching from remote and local"))
    }

    private suspend fun combineLocalDataWithRemoteData(remoteVideos: List<Video>): List<Video> {
        val localVideos = videosLocalDataSource.getVideos()
        if (localVideos is Success) {
            val newVideosMap = remoteVideos.associateBy { it.id }

            localVideos.data.forEach { localVideo ->
                val newVideo = newVideosMap[localVideo.id]
                if (newVideo != null) {
                    newVideo.reported = localVideo.reported
                    newVideo.excluded = localVideo.excluded
                }
            }
            return newVideosMap.values.toList()
        }

        return remoteVideos
    }

    private suspend fun combineLocalDataWithRemoteData(remoteVideo: Video): Video {
        val localVideo = videosLocalDataSource.getVideo(remoteVideo.id)
        if (localVideo is Success) {
            return remoteVideo.copy(
                reported = localVideo.data.reported,
                excluded = localVideo.data.excluded
            )
        }

        return remoteVideo
    }

    private fun refreshCache(videos: List<Video>) {
        cachedVideos?.clear()
        videos.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(videos: List<Video>) {
        videosLocalDataSource.deleteAllVideos()
        for (video in videos) {
            videosLocalDataSource.saveVideo(video)
        }
    }

    private suspend fun refreshLocalDataSource(video: Video) {
        videosLocalDataSource.saveVideo(video)
    }

    private fun cacheVideo(video: Video): Video {
        val cachedVideo = video.copy()
        // Create if it doesn't exist.
        if (cachedVideos == null) {
            cachedVideos = ConcurrentHashMap()
        }
        cachedVideos?.put(cachedVideo.id, cachedVideo)
        return cachedVideo
    }

    private inline fun cacheAndPerform(video: Video, perform: (Video) -> Unit) {
        val cachedVideo = cacheVideo(video)
        perform(cachedVideo)
    }

    override suspend fun reportVideo(video: Video) {
        cacheAndPerform(video) {
            it.reported = true
            coroutineScope {
                launch { videosLocalDataSource.reportVideo(it) }
            }
        }
    }

    override suspend fun excludeVideo(video: Video) {
        cacheAndPerform(video) {
            it.excluded = true
            coroutineScope {
                launch { videosLocalDataSource.excludeVideo(it) }
            }
        }
    }

    override suspend fun includeVideo(video: Video) {
        cacheAndPerform(video) {
            it.excluded = false
            coroutineScope {
                launch { videosLocalDataSource.includeVideo(it) }
            }
        }
    }

    override suspend fun getVideo(videoId: String, forceUpdate: Boolean): Result<Video> {

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available
            if (!forceUpdate) {
                getVideoWithId(videoId)?.let {
                    return@withContext Success(it)
                }
            }

            val newVideo = fetchVideoFromRemoteOrLocal(videoId, forceUpdate)

            // Refresh the cache with the new videos
            (newVideo as? Success)?.let { cacheVideo(it.data) }

            return@withContext newVideo
        }
    }

    private fun getVideoWithId(id: String) = cachedVideos?.get(id)

    private suspend fun fetchVideoFromRemoteOrLocal(
        videoId: String,
        forceUpdate: Boolean
    ): Result<Video> {
        // Remote first
        when (val remoteVideo = videosRemoteDataSource.getVideo(videoId)) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                val newVideo = combineLocalDataWithRemoteData(remoteVideo.data)
                refreshLocalDataSource(newVideo)
                return Success(newVideo)
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localVideo = videosLocalDataSource.getVideo(videoId)
        if (localVideo is Success) return localVideo
        return Error(Exception("Error fetching from remote and local"))
    }

    override suspend fun addBlacklistVideo(url: String, description: String): Result<Video> {
        return videosRemoteDataSource.addBlacklistVideo(
            YoutubeUrlUtils.extractVideoIdFromUrl(url)!!,
            description
        )
    }
}