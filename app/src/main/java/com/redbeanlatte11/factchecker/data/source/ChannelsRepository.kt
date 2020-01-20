package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result

interface ChannelsRepository {

    suspend fun getChannels(forceUpdate: Boolean = false): Result<List<Channel>>

    suspend fun getChannel(channelId: String, forceUpdate: Boolean = false): Result<Channel>

    suspend fun addBlacklistChannel(url: String, description: String): Result<Channel>
}