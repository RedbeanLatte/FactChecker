package com.redbeanlatte11.factchecker

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.redbeanlatte11.factchecker.data.source.*
import com.redbeanlatte11.factchecker.data.source.local.ChannelsLocalDataSource
import com.redbeanlatte11.factchecker.data.source.local.FactCheckerDataBase
import com.redbeanlatte11.factchecker.data.source.local.PopularVideosDataBase
import com.redbeanlatte11.factchecker.data.source.local.VideosLocalDataSource
import com.redbeanlatte11.factchecker.data.source.remote.FakeChannelsRemoteDataSource
import com.redbeanlatte11.factchecker.data.source.remote.FakePopularVideosRemoteDataSource
import com.redbeanlatte11.factchecker.data.source.remote.JsonParser

object ServiceLocator {

    private val lock = Any()
    private var database: FactCheckerDataBase? = null
    private var popularVideosDataBase: PopularVideosDataBase? = null

    @Volatile
    var videosRepository: VideosRepository? = null
        @VisibleForTesting set

    @Volatile
    var popularVideosRepository: VideosRepository? = null
        @VisibleForTesting set

    @Volatile
    var channelsRepository: ChannelsRepository? = null
        @VisibleForTesting set

    fun provideVideosRepository(context: Context): VideosRepository {
        synchronized(this) {
            if (videosRepository == null) {
                videosRepository = createVideosRepository(context)
            }
            return videosRepository as VideosRepository
        }
    }

    fun providePopularVideosRepository(context: Context): VideosRepository {
        synchronized(this) {
            if (popularVideosRepository == null) {
                popularVideosRepository = createPopularVideosRepository(context)
            }
            return popularVideosRepository as VideosRepository
        }
    }

    fun provideChannelsRepository(context: Context): ChannelsRepository {
        synchronized(this) {
            if (channelsRepository == null) {
                channelsRepository = createChannelsRepository(context)
            }
            return channelsRepository as ChannelsRepository
        }
    }

    //TODO: implement createVideosRepository
    private fun createVideosRepository(context: Context): VideosRepository {
        return DefaultVideosRepository(
//            ProductsRemoteDataSource(GarimFirestore(context)),
            FakePopularVideosRemoteDataSource(JsonParser.from(context, "test_popular_videos.json")),
            createVideosLocalDataSource(context)
        )
    }

    private fun createPopularVideosRepository(context: Context): VideosRepository {
        return DefaultVideosRepository(
            FakePopularVideosRemoteDataSource(JsonParser.from(context, "test_popular_videos.json")),
            createPopularVideosLocalDataSource(context)
        )
    }

    private fun createChannelsRepository(context: Context): ChannelsRepository? {
        return DefaultChannelsRepository(
            FakeChannelsRemoteDataSource(JsonParser.from(context, "channels.json")),
            createChannelsLocalDataSource(context)
        )
    }

    private fun createVideosLocalDataSource(context: Context): VideosDataSource {
        val database = database ?: createDataBase(context)
        return VideosLocalDataSource(database.videoDao())
    }

    private fun createPopularVideosLocalDataSource(context: Context): VideosDataSource {
        val database = popularVideosDataBase ?: createPopularVideosDataBase(context)
        return VideosLocalDataSource(database.videoDao())
    }

    private fun createChannelsLocalDataSource(context: Context): ChannelsDataSource {
        val database = database ?: createDataBase(context)
        return ChannelsLocalDataSource(database.channelDao())
    }

    private fun createDataBase(context: Context): FactCheckerDataBase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            FactCheckerDataBase::class.java, "FactCheckerDB.db"
        ).build()
        database = result
        return result
    }

    private fun createPopularVideosDataBase(context: Context): PopularVideosDataBase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            PopularVideosDataBase::class.java, "PopularVideos.db"
        ).build()
        popularVideosDataBase = result
        return result
    }
}
