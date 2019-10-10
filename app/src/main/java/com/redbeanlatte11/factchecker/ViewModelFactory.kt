package com.redbeanlatte11.factchecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redbeanlatte11.factchecker.channel.ChannelsViewModel
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import com.redbeanlatte11.factchecker.domain.*
import com.redbeanlatte11.factchecker.home.AddVideoBlacklistViewModel
import com.redbeanlatte11.factchecker.home.GoogleAccountViewModel
import com.redbeanlatte11.factchecker.home.VideosViewModel
import com.redbeanlatte11.factchecker.more.DonationViewModel
import com.redbeanlatte11.factchecker.popular.PopularViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val videosRepository: VideosRepository,
    private val popularVideosRepository: VideosRepository,
    private val channelsRepository: ChannelsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {

                isAssignableFrom(VideosViewModel::class.java) ->
                    VideosViewModel(
                        GetVideosUseCase(videosRepository),
                        ReportVideoUseCase(videosRepository),
                        ExcludeVideoUseCase(videosRepository),
                        IncludeVideoUseCase(videosRepository)
                    )

                isAssignableFrom(GoogleAccountViewModel::class.java) ->
                    GoogleAccountViewModel(
                        SignInUseCase()
                    )

                isAssignableFrom(PopularViewModel::class.java) ->
                    PopularViewModel(
                        GetPopularVideosUseCase(popularVideosRepository)
                    )

                isAssignableFrom(ChannelsViewModel::class.java) ->
                    ChannelsViewModel(
                        GetChannelsUseCase(channelsRepository)
                    )

                isAssignableFrom(DonationViewModel::class.java) ->
                    DonationViewModel(
                        DonateUseCase()
                    )

                isAssignableFrom(AddVideoBlacklistViewModel::class.java) ->
                    AddVideoBlacklistViewModel(
                        AddVideoBlacklistUseCase(
                            popularVideosRepository
                        )
                    )

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}