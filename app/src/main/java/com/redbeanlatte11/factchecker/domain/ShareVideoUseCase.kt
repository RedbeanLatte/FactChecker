package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.source.VideosRepository

class ShareVideoUseCase (
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke() {
    }
}