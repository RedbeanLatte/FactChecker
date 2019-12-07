package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.source.VideosRepository

class AddBlacklistVideoUseCase(
    private val popularVideosRepository: VideosRepository
) {

    suspend operator fun invoke(url: String, description: String) {
        popularVideosRepository.addVideoBlacklist(url, description)
    }
}