package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import com.redbeanlatte11.factchecker.ui.home.VideosFilterType
import com.redbeanlatte11.factchecker.ui.home.VideosFilterType.*

class GetVideosUseCase(
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentFilter: VideosFilterType = ALL_VIDEOS
    ): Result<List<Video>> {

        val videosResult = videosRepository.getVideos(forceUpdate)

        // Filter videos
        if (videosResult is Success && currentFilter != ALL_VIDEOS) {
            val videos = videosResult.data

            val videosToShow = mutableListOf<Video>()
            // We filter the videos based on the requestType
            for (video in videos) {
                when (currentFilter) {
                    BLACKLIST_VIDEOS -> if (!video.reported && !video.excluded) {
                        videosToShow.add(video)
                    }
                    REPORTED_VIDEOS -> if (video.reported) {
                        videosToShow.add(video)
                    }
                    EXCLUDED_VIDEOS -> if (video.excluded) {
                        videosToShow.add(video)
                    }
                    else -> NotImplementedError()
                }
            }
            return Success(videosToShow)
        }
        return videosResult
    }
}
