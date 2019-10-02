package com.redbeanlatte11.factchecker.data

import com.redbeanlatte11.factchecker.util.toAgoTime
import org.joda.time.DateTime

data class Video(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics
)

data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, Thumbnail>,
    val channelTitle: String,
    val tags: List<String>,
    val categoryId: String,
    val liveBroadcastContent: String,
    val localized: Localized
) {
    val thumbnailUrl: String?
        get() = thumbnails["high"]?.url

    val publishedAtToShow: String?
        get() = DateTime.parse(publishedAt).toAgoTime()
}