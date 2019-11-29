package com.redbeanlatte11.factchecker.data.source.remote

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class PopularVideosRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosDataSource {

    private val factCheckerService by lazy {
        FactCheckerService.create()
    }

    override suspend fun getVideos(): Result<List<Video>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(factCheckerService.getPopularVideos())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getVideo(videoId: String): Result<Video> {
        return Error(IllegalAccessException())
    }

    override suspend fun saveVideo(video: Video) {
        throw IllegalAccessException()
    }

    override suspend fun deleteAllVideos() {
        throw IllegalAccessException()
    }

    override suspend fun reportVideo(video: Video) {
        throw IllegalAccessException()
    }

    override suspend fun excludeVideo(video: Video) {
        throw IllegalAccessException()
    }

    override suspend fun includeVideo(video: Video) {
        throw IllegalAccessException()
    }
}