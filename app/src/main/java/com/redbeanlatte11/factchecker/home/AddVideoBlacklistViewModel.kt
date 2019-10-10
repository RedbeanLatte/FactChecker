package com.redbeanlatte11.factchecker.home

import androidx.lifecycle.ViewModel
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.domain.AddVideoBlacklistUseCase

class AddVideoBlacklistViewModel(
    private val addVideoBlacklistUseCase: AddVideoBlacklistUseCase
) : ViewModel() {

    fun addVideoBlacklist(video: Video) {
        addVideoBlacklistUseCase(video)
    }
}