package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository

class GetChannelsUseCase(
    private val channelsRepository: ChannelsRepository
) {
    suspend operator fun invoke(
        forceUpdate: Boolean = false
    ): Result<List<Channel>> {
        return channelsRepository.getChannels(forceUpdate)
    }
}