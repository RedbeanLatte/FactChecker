package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result

interface VideosRepository {

    suspend fun getVideos(forceUpdate: Boolean = false): Result<List<Video>>

    suspend fun reportVideo(video: Video)

    suspend fun excludeVideo(video: Video)

    suspend fun includeVideo(video: Video)
}