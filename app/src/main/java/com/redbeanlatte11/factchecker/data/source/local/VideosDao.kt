package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redbeanlatte11.factchecker.data.Video

@Dao
interface VideosDao {
    @Query("SELECT *, `id` FROM Videos")
    suspend fun getVideos(): List<Video>

    @Query("SELECT *, `id` FROM Videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: String): Video?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Query("DELETE FROM Videos")
    suspend fun deleteVideos()

    @Query("""
        SELECT *, `id` 
        FROM Videos 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        OR channelTitle LIKE '%' || :query || '%'
        OR tags LIKE '%' || :query || '%'
        OR localized LIKE '%' || :query || '%'
        """)
    suspend fun searchVideos(query: String): List<Video>

    @Query("UPDATE videos SET reported = :reported WHERE id = :videoId")
    suspend fun updateReported(videoId: String, reported: Boolean)

    @Query("UPDATE videos SET excluded = :excluded WHERE id = :videoId")
    suspend fun updateExcluded(videoId: String, excluded: Boolean)
}