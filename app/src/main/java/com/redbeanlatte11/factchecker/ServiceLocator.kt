package com.redbeanlatte11.factchecker

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.redbeanlatte11.factchecker.data.source.*
import com.redbeanlatte11.factchecker.data.source.local.ChannelsLocalDataSource
import com.redbeanlatte11.factchecker.data.source.local.VideosLocalDataSource
import com.redbeanlatte11.factchecker.data.source.remote.FakeChannelsRemoteDataSource
import com.redbeanlatte11.factchecker.data.source.remote.FakePopularVideosRemoteDataSource
import com.redbeanlatte11.factchecker.data.source.remote.JsonParser

object ServiceLocator {

    private val lock = Any()
//    private var database: GarimDatabase? = null

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
            createVideosLocalDataSource(context)
        )
    }

    private fun createChannelsRepository(context: Context): ChannelsRepository? {
        return DefaultChannelsRepository(
            FakeChannelsRemoteDataSource(JsonParser.from(context, "channels.json")),
            ChannelsLocalDataSource()
        )
    }

//
    private fun createVideosLocalDataSource(context: Context): VideosDataSource {
//        val database = database ?: createDataBase(context)
//        return ProductsLocalDataSource(database.productDao())
        return VideosLocalDataSource()
    }
//
//    private fun createDataBase(context: Context): GarimDatabase {
//        val result = Room.databaseBuilder(
//            context.applicationContext,
//            GarimDatabase::class.java, "Products.db"
//        ).build()
//        database = result
//        return result
//    }
}
