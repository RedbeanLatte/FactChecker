package com.redbeanlatte11.factchecker.data.source.remote

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.source.ChannelsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

open class ChannelsRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChannelsDataSource {

    private val factCheckerService by lazy {
        FactCheckerService.create()
    }

    override suspend fun getChannels(): Result<List<Channel>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(factCheckerService.getChannels())
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun addBlacklistChannel(
        channelId: String?,
        userName: String?,
        description: String
    ): Result<Channel> = withContext(ioDispatcher) {
        return@withContext try {
            Success(factCheckerService.addBlacklistChannel(channelId, userName, description))
        } catch (e: Exception) {
            Error(e)
        }
    }
}