package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redbeanlatte11.factchecker.data.Channel

@Dao
interface ChannelsDao {
    @Query("SELECT *, `id` FROM Channels")
    suspend fun getChannels(): List<Channel>

    @Query("SELECT *, `id` FROM Channels WHERE id = :channelId")
    suspend fun getChannelById(channelId: String): Channel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: Channel)

    @Query("DELETE FROM Channels")
    suspend fun deleteChannels()

    @Query("""
        SELECT *, `id` 
        FROM Channels 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        OR localized LIKE '%' || :query || '%'
        """)
    suspend fun searchChannels(query: String): List<Channel>

    @Query("UPDATE channels SET watched = :watched WHERE id = :channelId")
    suspend fun updateWatched(channelId: String, watched: Boolean)
}