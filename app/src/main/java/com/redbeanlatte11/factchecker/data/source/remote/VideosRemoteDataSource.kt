package com.redbeanlatte11.factchecker.data.source.remote

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

open class VideosRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosDataSource {

    private val factCheckerService by lazy {
        FactCheckerService.create()
    }

    override suspend fun getVideos(
        offset: Int,
        limit: Int,
        watchedChannels: List<Channel>
    ): Result<List<Video>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(factCheckerService.getVideos(offset, limit, watchedChannels))
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

    suspend fun addBlacklistVideo(videoId: String, description: String): Result<Video> = withContext(ioDispatcher) {
        return@withContext try {
            Success(factCheckerService.addBlacklistVideo(videoId, description))
        } catch (e: Exception) {
            Error(e)
        }
    }
}