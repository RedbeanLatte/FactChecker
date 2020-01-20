package com.redbeanlatte11.factchecker.data

import androidx.room.*
import com.redbeanlatte11.factchecker.util.OrderingByKoreanEnglishUtils
import com.redbeanlatte11.factchecker.util.toSummaryCount
import org.joda.time.DateTime

@Entity(tableName = "channels")
data class Channel(
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "etag") val etag: String,
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @Embedded val snippet: ChannelSnippet,
    @Embedded val statistics: ChannelStatistics,
    @ColumnInfo(name = "createdAt") val createdAt: String = DateTime.now().toString()
) {
    val youtubeUrl: String?
        get() = "http://m.youtube.com/channel/$id"

    object TitleComparator : Comparator<Channel> {

        override fun compare(c1: Channel, c2: Channel) : Int {
            return OrderingByKoreanEnglishUtils.compare(c1.snippet.title, c2.snippet.title)
        }
    }
}

data class ChannelSnippet(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "publishedAt") val publishedAt: String,
    @ColumnInfo(name = "thumbnails") val thumbnails: Map<String, Thumbnail>,
    @ColumnInfo(name = "localized") val localized: Map<String, String>,
    @ColumnInfo(name = "country") val country: String? = "KR"
) {
    val thumbnailUrl: String?
        get() = thumbnails["medium"]?.url
}

data class ChannelStatistics(
    @ColumnInfo(name = "viewCount") val viewCount: Int,
    @ColumnInfo(name = "commentCount") val commentCount: Int,
    @ColumnInfo(name = "subscriberCount") val subscriberCount: Int,
    @ColumnInfo(name = "hiddenSubscriberCount") val hiddenSubscriberCount: Boolean,
    @ColumnInfo(name = "videoCount") val videoCount: Int
) {
    val subscriberCountAndVideoCountToShow: String?
        get() = if(subscriberCount > 0) {
            "구독자 ${subscriberCount.toSummaryCount()}명 · 동영상 ${videoCount.toSummaryCount()}개"
        } else {
            "동영상 ${videoCount.toSummaryCount()}"
        }
}