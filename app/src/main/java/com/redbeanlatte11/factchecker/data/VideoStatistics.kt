package com.redbeanlatte11.factchecker.data

import com.redbeanlatte11.factchecker.util.toSummuryCount

data class VideoStatistics(
    val viewCount: Int,
    val likeCount: Int,
    val dislikeCount: Int,
    val favoriteCount: Int,
    val commentCount: Int
) {
    val viewCountToShow
        get() = viewCount.toSummuryCount()
}