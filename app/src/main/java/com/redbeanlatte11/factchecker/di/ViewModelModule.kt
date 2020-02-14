package com.redbeanlatte11.factchecker.di

import com.redbeanlatte11.factchecker.ui.channel.ChannelsViewModel
import com.redbeanlatte11.factchecker.domain.*
import com.redbeanlatte11.factchecker.ui.channel.AddBlacklistChannelViewModel
import com.redbeanlatte11.factchecker.ui.home.AddBlacklistVideoViewModel
import com.redbeanlatte11.factchecker.ui.home.GoogleAccountViewModel
import com.redbeanlatte11.factchecker.ui.home.VideosViewModel
import com.redbeanlatte11.factchecker.ui.more.DonationViewModel
import com.redbeanlatte11.factchecker.ui.popular.PopularViewModel
import com.redbeanlatte11.factchecker.ui.setup.SetupViewModel
import com.redbeanlatte11.factchecker.ui.share.ShareViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SetupViewModel()
    }

    viewModel {
        VideosViewModel(
            GetVideosUseCase(get(named("blacklist_videos_repository"))),
            ReportVideoUseCase(get(named("blacklist_videos_repository"))),
            ExcludeVideoUseCase(get(named("blacklist_videos_repository"))),
            IncludeVideoUseCase(get(named("blacklist_videos_repository")))
        )
    }

    viewModel {
        GoogleAccountViewModel(SignInUseCase())
    }

    viewModel {
        PopularViewModel(GetPopularVideosUseCase(get(named("popular_videos_repository"))))
    }

    viewModel {
        ChannelsViewModel(GetChannelsUseCase(get()))
    }

    viewModel {
        DonationViewModel(DonateUseCase())
    }

    viewModel {
        AddBlacklistVideoViewModel(
            AddBlacklistVideoUseCase(get(named("blacklist_videos_repository"))),
            GetVideoUseCase(get(named("blacklist_videos_repository")))
        )
    }

    viewModel {
        AddBlacklistChannelViewModel(
            AddBlacklistChannelUseCase(get()),
            GetChannelUseCase(get())
        )
    }

    viewModel {
        ShareViewModel(
            AddBlacklistVideoUseCase(get(named("blacklist_videos_repository"))),
            GetVideoUseCase(get(named("blacklist_videos_repository"))),
            AddBlacklistChannelUseCase(get()),
            GetChannelUseCase(get())
        )
    }
}