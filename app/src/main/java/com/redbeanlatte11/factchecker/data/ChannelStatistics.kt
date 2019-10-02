package com.redbeanlatte11.factchecker.data

import com.redbeanlatte11.factchecker.util.toSummuryCount

data class ChannelStatistics(
    val viewCount: Int,
    val commentCount: Int,
    val subscriberCount: Int,
    val hiddenSubscriberCount: Boolean,
    val videoCount: Int
) {
    val subscriberCountToShow
        get() = viewCount.toSummuryCount()

    val videoCountToShow
        get() = videoCount.toSummuryCount()
}