package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.source.VideosRepository

class GetVideoUseCase(
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke(videoId: String, forceUpdate: Boolean = false): Result<Video> {
        return videosRepository.getVideo(videoId, forceUpdate)
    }
}