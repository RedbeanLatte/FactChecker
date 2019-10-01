package com.redbeanlatte11.factchecker.data.source

import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Result

interface ChannelsDataSource {
    fun getChnnels(): Result<List<Channel>>
}