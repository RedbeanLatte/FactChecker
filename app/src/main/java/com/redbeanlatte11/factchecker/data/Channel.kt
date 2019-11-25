package com.redbeanlatte11.factchecker.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.redbeanlatte11.factchecker.util.toSummaryCount

@Entity(tableName = "channels")
data class Channel(
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "etag") val etag: String,
    @PrimaryKey @ColumnInfo(name = "entryid") val id: String,
    @Embedded val snippet: ChannelSnippet,
    @Embedded val statistics: ChannelStatistics
) {
    val youtubeUrl: String?
        get() = "http://m.youtube.com/channel/$id"
}

data class ChannelSnippet(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "customUrl") val customUrl: String,
    @ColumnInfo(name = "publishedAt") val publishedAt: String,
    @ColumnInfo(name = "thumbnails") val thumbnails: Map<String, Thumbnail>,
    @ColumnInfo(name = "localized") val localized: Map<String, String>,
    @ColumnInfo(name = "country") val country: String
) {
    val thumbnailUrl: String?
        get() = thumbnails["high"]?.url
}

data class ChannelStatistics(
    @ColumnInfo(name = "viewCount") val viewCount: Int,
    @ColumnInfo(name = "commentCount") val commentCount: Int,
    @ColumnInfo(name = "subscriberCount") val subscriberCount: Int,
    @ColumnInfo(name = "hiddenSubscriberCount") val hiddenSubscriberCount: Boolean,
    @ColumnInfo(name = "videoCount") val videoCount: Int
) {
    val subscriberCountToShow: String?
        get() = "구독자 ${subscriberCount.toSummaryCount()}명"

    val videoCountToShow: String?
        get() = "동영상 ${videoCount.toSummaryCount()}개"
}