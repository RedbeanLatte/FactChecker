package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository

class UnwatchChannelUseCase(
    private val channelsRepository: ChannelsRepository
) {
    suspend operator fun invoke(channel: Channel) {
        channelsRepository.unwatchChannel(channel)
    }
}