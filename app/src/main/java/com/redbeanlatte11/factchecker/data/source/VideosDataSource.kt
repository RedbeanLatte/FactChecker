package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result

interface VideosDataSource {
    suspend fun getVideos(): Result<List<Video>>

    suspend fun saveVideo(video: Video)

    suspend fun deleteAllVideos()
}