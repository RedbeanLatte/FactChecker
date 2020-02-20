package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Success
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository

class GetWatchedChannelsUseCase(
    private val channelsRepository: ChannelsRepository
) {

    suspend operator fun invoke(
        forceUpdate: Boolean = false
    ): Result<List<Channel>> {
        val channelsResult = channelsRepository.getChannels(forceUpdate)

        // Filter channels
        if (channelsResult is Success) {
            val channels = channelsResult.data

            val channelsToShow = channels.filter { it.watched }
            return Success(channelsToShow)
        }

        return channelsResult
    }
}