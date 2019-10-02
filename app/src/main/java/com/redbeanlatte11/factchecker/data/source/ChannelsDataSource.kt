package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result

interface ChannelsDataSource {

    suspend fun getChannels(): Result<List<Channel>>

    suspend fun saveChannel(channel: Channel)

    suspend fun deleteAllChannels()
}