package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result

interface VideosDataSource {

    suspend fun getVideos(
        offset: Int = DEFAULT_OFFSET,
        limit: Int = DEFAULT_LIMIT,
        watchedChannels: List<Channel> = emptyList()
    ): Result<List<Video>>

    suspend fun getVideo(videoId: String): Result<Video>

    companion object {

        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 10
    }
}