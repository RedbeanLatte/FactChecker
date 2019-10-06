package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redbeanlatte11.factchecker.data.Channel

@Dao
interface ChannelsDao {
    @Query("SELECT *, `entryid` FROM Channels")
    suspend fun getChannels(): List<Channel>

    @Query("SELECT *, `entryid` FROM Channels WHERE entryid = :channelId")
    suspend fun getChannelById(channelId: String): Channel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: Channel)

    @Query("DELETE FROM Channels")
    suspend fun deleteChannels()

    @Query("""
        SELECT *, `entryid` 
        FROM Channels 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        OR localized LIKE '%' || :query || '%'
        """)
    suspend fun searchChannels(query: String): List<Channel>
}