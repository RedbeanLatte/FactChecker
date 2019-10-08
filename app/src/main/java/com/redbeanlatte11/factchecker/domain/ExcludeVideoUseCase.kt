package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository

class ExcludeVideoUseCase(
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke(video: Video) {
        videosRepository.excludeVideo(video)
    }
}