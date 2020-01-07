package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result

interface VideosRepository {

    suspend fun getVideos(forceUpdate: Boolean = false): Result<List<Video>>

    suspend fun getVideo(videoId: String, forceUpdate: Boolean = false): Result<Video>

    suspend fun reportVideo(video: Video)

    suspend fun excludeVideo(video: Video)

    suspend fun includeVideo(video: Video)

    suspend fun addBlacklistVideo(url: String, description: String): Result<Video>
}