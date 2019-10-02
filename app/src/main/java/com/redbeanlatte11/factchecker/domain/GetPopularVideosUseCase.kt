package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.source.VideosRepository

class GetPopularVideosUseCase(
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentCategoryId: String = "25" // News & Politics
    ): Result<List<Video>> {

        val videosResult = videosRepository.getVideos(forceUpdate)

        // Filter products
        if (videosResult is Success) {
            val videos = videosResult.data

            val videosToShow = mutableListOf<Video>()
            // We filter the products based on the requestType
            for (video in videos) {
                if (video.snippet.categoryId == currentCategoryId) {
                    videosToShow.add(video)
                }
            }
            return Success(videosToShow)
        }
        return videosResult
    }
}