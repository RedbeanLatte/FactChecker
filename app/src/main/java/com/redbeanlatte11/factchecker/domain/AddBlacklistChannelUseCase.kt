package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository

class AddBlacklistChannelUseCase(
    private val channelsRepository: ChannelsRepository
) {

    suspend operator fun invoke(url: String, description: String): Result<Channel> {
        return channelsRepository.addBlacklistChannel(url, description)
    }
}