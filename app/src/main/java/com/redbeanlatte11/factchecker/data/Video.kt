package com.redbeanlatte11.factchecker.data

data class Video(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: VideoSnippet
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
    val defaultLanguage: String,
    val localized: Localized,
    val defaultAudioLanguage: String
) {
    val thumbnailUrl: String?
        get() = thumbnails["high"]?.url
}