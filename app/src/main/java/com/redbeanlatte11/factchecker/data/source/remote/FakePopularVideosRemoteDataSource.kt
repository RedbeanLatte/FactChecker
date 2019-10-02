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

class FakePopularVideosRemoteDataSource(
    private val jsonParser: JsonParser,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosDataSource {

    override suspend fun getVideos(): Result<List<Video>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(jsonParser.getPopularVideos())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveVideo(video: Video) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllVideos() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}