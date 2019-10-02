package com.redbeanlatte11.factchecker.data

data class Channel(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: ChannelSnippet,
    val statistics: ChannelStatistics
)

data class ChannelSnippet(
    val title: String,
    val description: String,
    val customUrl: String,
    val publishedAt: String,
    val thumbnails: Map<String, Thumbnail>,
    val localized: Localized,
    val country: String
) {
    val thumbnailUrl: String?
        get() = thumbnails["high"]?.url
}
