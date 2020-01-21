package com.redbeanlatte11.factchecker.data

import androidx.room.*
import com.redbeanlatte11.factchecker.util.toAgoTime
import com.redbeanlatte11.factchecker.util.toSummaryCount
import org.joda.time.DateTime

@Entity(tableName = "videos")
data class Video(
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "etag") val etag: String,
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @Embedded val snippet: VideoSnippet,
    @Embedded val statistics: VideoStatistics,
    @ColumnInfo(name = "createdAt") val createdAt: String = DateTime.now().toString(),
    @ColumnInfo(name = "reported") var reported: Boolean = false,
    @ColumnInfo(name = "excluded") var excluded: Boolean = false
) {

    val youtubeUrl: String?
        get() = "https://m.youtube.com/watch?v=$id"

    @delegate:Ignore
    val createdAtDateTime: DateTime by lazy {
        DateTime.parse(createdAt)
    }

    @delegate:Ignore
    val publishedAtDateTime: DateTime by lazy {
        DateTime.parse(snippet.publishedAt)
    }

    companion object {

        fun getComparator(sortType: SortType): Comparator<Video> =
            when(sortType) {
                SortType.PUBLISHED_AT -> PublishedAtComparator
                SortType.CREATED_AT -> CreatedAtComparator
            }

        object PublishedAtComparator : Comparator<Video> {

            override fun compare(v1: Video, v2: Video): Int {
                return v2.publishedAtDateTime.compareTo(v1.publishedAtDateTime)
            }
        }

        object CreatedAtComparator : Comparator<Video> {

            override fun compare(v1: Video, v2: Video): Int {
                return v2.createdAtDateTime.compareTo(v1.createdAtDateTime)
            }
        }
    }

    enum class SortType {
        PUBLISHED_AT,
        CREATED_AT
    }
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
        get() = "조회수 ${viewCount.toSummaryCount()}"
}