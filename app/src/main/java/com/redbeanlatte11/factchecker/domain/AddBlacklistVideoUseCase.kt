package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository

class AddBlacklistVideoUseCase(
    private val videosRepository: VideosRepository
) {

    suspend operator fun invoke(url: String, description: String = ""): Result<Video> {
        return videosRepository.addBlacklistVideo(url, description)
    }
}