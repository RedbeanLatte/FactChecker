package com.redbeanlatte11.factchecker.di

import androidx.room.Room
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository
import com.redbeanlatte11.factchecker.data.source.DefaultChannelsRepository
import com.redbeanlatte11.factchecker.data.source.DefaultVideosRepository
import com.redbeanlatte11.factchecker.data.source.local.ChannelsLocalDataSource
import com.redbeanlatte11.factchecker.data.source.local.FactCheckerDataBase
import com.redbeanlatte11.factchecker.data.source.local.PopularVideosDataBase
import com.redbeanlatte11.factchecker.data.source.local.VideosLocalDataSource
import com.redbeanlatte11.factchecker.data.source.remote.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    single(named("blacklist_videos_repository")) {
        DefaultVideosRepository(
            VideosRemoteDataSource(),
//            FakePopularVideosRemoteDataSource(JsonParser.from(androidContext(), "test_popular_videos.json")),
            get(named("blacklist_videos_local_data_source"))
        )
    }

    single(named("popular_videos_repository")) {
        DefaultVideosRepository(
            PopularVideosRemoteDataSource(),
//            FakePopularVideosRemoteDataSource(JsonParser.from(androidContext(), "test_popular_videos.json")),
            get(named("popular_videos_local_data_source"))
        )
    }

    single {
        DefaultChannelsRepository(
            ChannelsRemoteDataSource(),
//            FakeChannelsRemoteDataSource(JsonParser.from(androidContext(), "channels.json")),
            get(named("channels_local_data_source"))
        ) as ChannelsRepository
    }

    single(named("blacklist_videos_local_data_source")) {
        VideosLocalDataSource(get<FactCheckerDataBase>().videoDao())
    }

    single(named("popular_videos_local_data_source")) {
        VideosLocalDataSource(get<PopularVideosDataBase>().videoDao())
    }

    single(named("channels_local_data_source")) {
        ChannelsLocalDataSource(get<FactCheckerDataBase>().channelDao())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            FactCheckerDataBase::class.java, "FactCheckerDB.db"
        ).build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            PopularVideosDataBase::class.java, "PopularVideos.db"
        ).build()
    }
}
