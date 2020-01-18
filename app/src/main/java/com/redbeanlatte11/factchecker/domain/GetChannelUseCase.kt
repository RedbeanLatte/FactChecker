package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository

class GetChannelUseCase(
    private val channelsRepository: ChannelsRepository
) {
    suspend operator fun invoke(channelId: String): Result<Channel> {
        return channelsRepository.getChannel(channelId)
    }
}