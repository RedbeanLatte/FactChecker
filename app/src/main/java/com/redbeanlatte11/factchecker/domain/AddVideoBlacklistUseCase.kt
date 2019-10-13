package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository

class AddVideoBlacklistUseCase(
    private val popluarVideosRepository: VideosRepository
) {

    operator fun invoke(url: String, description: String) {

    }
}