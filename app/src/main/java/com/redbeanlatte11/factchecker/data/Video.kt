package com.redbeanlatte11.factchecker.data

import androidx.room.*
import com.redbeanlatte11.factchecker.data.source.local.LocalizedConverter
import com.redbeanlatte11.factchecker.data.source.local.StringListConverter
import com.redbeanlatte11.factchecker.data.source.local.ThumbnailsConverter
import com.redbeanlatte11.factchecker.util.toAgoTime
import com.redbeanlatte11.factchecker.util.toSummuryCount
import org.joda.time.DateTime

@Entity(tableName = "videos")
data class Video(
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "etag") val etag: String,
    @PrimaryKey @ColumnInfo(name = "entryid") val id: String,
    @Embedded val snippet: VideoSnippet,
    @Embedded val statistics: VideoStatistics,
    @ColumnInfo(name = "reported") var reported: Boolean = false,
    @ColumnInfo(name = "excluded") var excluded: Boolean = false
) {
    val youtubeUrl: String?
        get() = "https://m.youtube.com/watch?v=$id"
}

data class VideoSnippet(
    @ColumnInfo(name = "publishedAt") val publishedAt: String,
    @ColumnInfo(name = "channelId") val channelId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "thumbnails") val thumbnails: Map<String, Thumbnail>,
    @ColumnInfo(name = "channelTitle") val channelTitle: String,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "categoryId") val categoryId: String,
    @ColumnInfo(name = "liveBroadcastContent") val liveBroadcastContent: String,
    @ColumnInfo(name = "localized") val localized: Map<String, String>
) {
    val thumbnailUrl: String?
        get() = thumbnails["high"]?.url

    val publishedAtToShow: String?
        get() = DateTime.parse(publishedAt).toAgoTime()
}

data class VideoStatistics(
    @ColumnInfo(name = "viewCount") val viewCount: Int,
    @ColumnInfo(name = "likeCount") val likeCount: Int,
    @ColumnInfo(name = "dislikeCount") val dislikeCount: Int,
    @ColumnInfo(name = "favoriteCount") val favoriteCount: Int,
    @ColumnInfo(name = "commentCount") val commentCount: Int
) {
    val viewCountToShow
        get() = viewCount.toSummuryCount()
}