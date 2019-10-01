package com.redbeanlatte11.factchecker

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.redbeanlatte11.factchecker.data.source.DefaultVideosRepository
import com.redbeanlatte11.factchecker.data.source.VideosDataSource
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import com.redbeanlatte11.factchecker.data.source.local.VideosLocalDataSource
import com.redbeanlatte11.factchecker.data.source.remote.FakeVideosRemoteDataSource
import com.redbeanlatte11.factchecker.data.source.remote.JsonParser

object ServiceLocator {

    private val lock = Any()
//    private var database: GarimDatabase? = null
    @Volatile
    var videosRepository: VideosRepository? = null
        @VisibleForTesting set

    fun provideProductsRepository(context: Context): VideosRepository {
        synchronized(this) {
            if (videosRepository == null) {
                videosRepository = createVideosRepository(context)
            }
            return videosRepository as VideosRepository
        }
    }

    private fun createVideosRepository(context: Context): VideosRepository {
        return DefaultVideosRepository(
//            ProductsRemoteDataSource(GarimFirestore(context)),
            FakeVideosRemoteDataSource(JsonParser.from(context, "popular_videos.json")),
            createVideosLocalDataSource(context)
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
