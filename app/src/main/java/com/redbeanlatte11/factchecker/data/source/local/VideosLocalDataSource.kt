package com.redbeanlatte11.factchecker.data.source.local

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosLocalDataSource(
    private val videosDao: VideosDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VideosDataSource {

    override suspend fun getVideos(): Result<List<Video>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(videosDao.getVideos())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getVideo(videoId: String): Result<Video> = withContext(ioDispatcher) {
        try {
            val video = videosDao.getVideoById(videoId)
            if (video != null) {
                return@withContext Success(video)
            } else {
                return@withContext Error(Exception("Video not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun saveVideo(video: Video) = withContext(ioDispatcher) {
        videosDao.insertVideo(video)
    }

    override suspend fun deleteAllVideos() {
        videosDao.deleteVideos()
    }

    override suspend fun reportVideo(video: Video) {
        videosDao.updateReported(video.id, true)
    }

    override suspend fun excludeVideo(video: Video) {
        videosDao.updateExcluded(video.id, true)
    }

    override suspend fun includeVideo(video: Video) {
        videosDao.updateExcluded(video.id, false)
    }
}