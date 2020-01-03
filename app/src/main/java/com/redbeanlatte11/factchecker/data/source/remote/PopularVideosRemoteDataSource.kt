package com.redbeanlatte11.factchecker.data.source.remote

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class PopularVideosRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosRemoteDataSource() {

    private val factCheckerService by lazy {
        FactCheckerService.create()
    }

    override suspend fun getVideos(): Result<List<Video>> = withContext(ioDispatcher) {
        Timber.d("getVideos popularvideos");

        return@withContext try {
            Success(factCheckerService.getPopularVideos())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getVideo(videoId: String): Result<Video> = withContext(ioDispatcher) {
        return@withContext try {
            Success(factCheckerService.getVideo(videoId))
        } catch (e: Exception) {
            Error(e)
        }
    }
}