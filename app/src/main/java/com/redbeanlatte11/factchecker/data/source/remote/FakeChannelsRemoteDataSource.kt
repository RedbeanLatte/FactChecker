package com.redbeanlatte11.factchecker.data.source.remote

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.source.ChannelsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeChannelsRemoteDataSource(
    private val jsonParser: JsonParser,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChannelsDataSource {

    override suspend fun getChannels(): Result<List<Channel>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(jsonParser.getChannels())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveChannel(channel: Channel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllChannels() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}