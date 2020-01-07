package com.redbeanlatte11.factchecker.data.source.local

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.source.ChannelsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelsLocalDataSource(
    private val channelsDao: ChannelsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChannelsDataSource {

    override suspend fun getChannels(): Result<List<Channel>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(channelsDao.getChannels())
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getChannel(channelId: String): Result<Channel> = withContext(ioDispatcher) {
        try {
            val channel = channelsDao.getChannelById(channelId)
            if (channel != null) {
                return@withContext Success(channel)
            } else {
                return@withContext Error(Exception("Channel not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    suspend fun saveChannel(channel: Channel) = withContext(ioDispatcher) {
        channelsDao.insertChannel(channel)
    }

    suspend fun deleteAllChannels() {
        channelsDao.deleteChannels()
    }
}