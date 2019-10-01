package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.home.VideosFilterType
import com.redbeanlatte11.factchecker.home.VideosFilterType.ALL_VIDEOS
import com.redbeanlatte11.factchecker.data.source.VideosRepository

class GetVideosUseCase(
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentFiltering: VideosFilterType = ALL_VIDEOS
    ): Result<List<Video>> {

        val productsResult = videosRepository.getVideos(forceUpdate)

        // Filter products
        if (productsResult is Success && currentFiltering != ALL_VIDEOS) {
            val products = productsResult.data

            val productsToShow = mutableListOf<Video>()
            // We filter the products based on the requestType
            for (product in products) {
                when (currentFiltering) {
                    else -> NotImplementedError()
                }
            }
            return Success(productsToShow)
        }
        return productsResult
    }
}