package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import com.redbeanlatte11.factchecker.ui.home.SearchPeriod
import com.redbeanlatte11.factchecker.ui.home.VideosFilterType
import org.joda.time.DateTime
import org.joda.time.Duration

class GetVideosUseCase(
    private val videosRepository: VideosRepository
) {
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentFilter: VideosFilterType = VideosFilterType.ALL_VIDEOS,
        currentSearchPeriod: SearchPeriod = SearchPeriod.ALL
    ): Result<List<Video>> {

        val videosResult = videosRepository.getVideos(forceUpdate)

        // Filter videos
        if (videosResult is Success && currentFilter != VideosFilterType.ALL_VIDEOS) {
            val videos = videosResult.data

            val videosToShow = mutableListOf<Video>()
            // We filter the videos based on the requestType
            for (video in videos) {
                when (currentFilter) {
                    VideosFilterType.BLACKLIST_VIDEOS -> if (!video.reported && !video.excluded) {
                        videosToShow.add(video)
                    }
                    VideosFilterType.REPORTED_VIDEOS -> if (video.reported) {
                        videosToShow.add(video)
                    }
                    VideosFilterType.EXCLUDED_VIDEOS -> if (video.excluded) {
                        videosToShow.add(video)
                    }
                    else -> NotImplementedError()
                }
            }

            return Success(
                videosToShow.filter {
                    val diffDuration = Duration(it.createdAtDateTime, DateTime.now())
                    diffDuration.isShorterThan(currentSearchPeriod.duration)
                }
            )
        }
        return videosResult
    }
}
