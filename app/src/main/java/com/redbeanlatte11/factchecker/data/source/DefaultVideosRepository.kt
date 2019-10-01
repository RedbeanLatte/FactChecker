package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Video
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DefaultVideosRepository(
    private val videosRemoteDataSource: VideosDataSource,
    private val videosLocalDataSource: VideosDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosRepository {

    private var cachedVideos: ConcurrentMap<String, Video>? = null

    override suspend fun getVideos(
        forceUpdate: Boolean
    ): Result<List<Video>> {

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {
                cachedVideos?.let { cachedVideos ->
                    return@withContext Success(cachedVideos.values.sortedBy { it.id })
                }
            }

            val newVideos = fetchVideosFromRemoteOrLocal(forceUpdate)

            // Refresh the cache with the new products
            (newVideos as? Success)?.let { refreshCache(it.data) }

            cachedVideos?.values?.let { products ->
                return@withContext Success(products.sortedBy { it.id })
            }

            (newVideos as? Success)?.let {
                if (it.data.isEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(IllegalStateException("Illegal stete"))
        }
    }

    private suspend fun fetchVideosFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Video>> {
        // Remote first
        val remoteVideos = videosRemoteDataSource.getVideos()
        when (remoteVideos) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteVideos.data)
                return remoteVideos
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

    private fun refreshCache(products: List<Video>) {
        cachedVideos?.clear()
        products.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(products: List<Video>) {
        videosLocalDataSource.deleteAllVideos()
        for (product in products) {
            videosLocalDataSource.saveVideo(product)
        }
    }

    private suspend fun refreshLocalDataSource(product: Video) {
        videosLocalDataSource.saveVideo(product)
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
        val cachedTask = cacheVideo(video)
        perform(cachedTask)
    }
}