package com.redbeanlatte11.factchecker.data.source.local

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.source.ChannelsDataSource
import java.lang.IllegalStateException

class ChannelsLocalDataSource : ChannelsDataSource {
    override suspend fun getChannels(): Result<List<Channel>> {
        return Error(IllegalStateException())
    }

    override suspend fun saveChannel(channel: Channel) {
    }

    override suspend fun deleteAllChannels() {
    }

}