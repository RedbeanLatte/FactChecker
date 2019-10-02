package com.redbeanlatte11.factchecker

import android.app.Application
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FactCheckerApplication : Application() {

    val videoRepository: VideosRepository
        get() = ServiceLocator.provideVideosRepository(this)

    val poplarVideoRepository: VideosRepository
        get() = ServiceLocator.providePopularVideosRepository(this)

    val channelRepository: ChannelsRepository
        get() = ServiceLocator.provideChannelsRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        JodaTimeAndroid.init(this)
        startKoin {
            androidContext(this@FactCheckerApplication)
            modules(appModule)
        }
    }
}