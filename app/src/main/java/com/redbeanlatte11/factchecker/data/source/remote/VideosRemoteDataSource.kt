package com.redbeanlatte11.factchecker.data.source.remote

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosDataSource

class VideosRemoteDataSource : VideosDataSource {

    override suspend fun getVideos(): Result<List<Video>> {
        return Result.Error(IllegalAccessException())
    }

    override suspend fun saveVideo(video: Video) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllVideos() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}