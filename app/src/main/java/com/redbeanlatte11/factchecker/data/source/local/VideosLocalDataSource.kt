package com.redbeanlatte11.factchecker.data.source.local

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import java.lang.IllegalStateException

class VideosLocalDataSource : VideosDataSource {

    override suspend fun getVideos(): Result<List<Video>> {
        return Error(IllegalStateException())
    }

    override suspend fun saveVideo(video: Video) {
    }

    override suspend fun deleteAllVideos() {
    }
}