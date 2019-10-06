package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DefaultChannelsRepository(
    private val channelsRemoteDataSource: ChannelsDataSource,
    private val channelsLocalDataSource: ChannelsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChannelsRepository {

    private var cachedChannels: ConcurrentMap<String, Channel>? = null

    override suspend fun getChannels(
        forceUpdate: Boolean
    ): Result<List<Channel>> {

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {
                cachedChannels?.let { cachedChannels ->
                    return@withContext Success(cachedChannels.values.toList())
                }
            }

            val newChannels = fetchChannelsFromRemoteOrLocal(forceUpdate)

            // Refresh the cache with the new channels
            (newChannels as? Success)?.let { refreshCache(it.data) }

            cachedChannels?.values?.let { channels ->
                return@withContext Success(channels.toList())
            }

            (newChannels as? Success)?.let {
                if (it.data.isEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Result.Error(IllegalStateException("Illegal stete"))
        }
    }

    private suspend fun fetchChannelsFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Channel>> {
        // Remote first
        val remoteChannels = channelsRemoteDataSource.getChannels()
        when (remoteChannels) {
            is Result.Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteChannels.data)
                return remoteChannels
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localChannels = channelsLocalDataSource.getChannels()
        if (localChannels is Success) return localChannels
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(channels: List<Channel>) {
        cachedChannels?.clear()
        channels.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(channels: List<Channel>) {
        channelsLocalDataSource.deleteAllChannels()
        for (channel in channels) {
            channelsLocalDataSource.saveChannel(channel)
        }
    }

    private fun cacheChannel(channel: Channel): Channel {
        val cachedChannel = channel.copy()
        // Create if it doesn't exist.
        if (cachedChannels == null) {
            cachedChannels = ConcurrentHashMap()
        }
        cachedChannels?.put(cachedChannel.id, cachedChannel)
        return cachedChannel
    }

    private inline fun cacheAndPerform(channel: Channel, perform: (Channel) -> Unit) {
        val cachedChannel = cacheChannel(channel)
        perform(cachedChannel)
    }
}