package com.redbeanlatte11.factchecker

import android.app.Application
import com.redbeanlatte11.factchecker.data.source.ChannelsRepository
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import timber.log.Timber

class FactCheckerApplication : Application() {

    val videoRepository: VideosRepository by inject(named("blacklist"))

    val poplarVideoRepository: VideosRepository by inject(named("popular"))

    val channelRepository: ChannelsRepository by inject()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        JodaTimeAndroid.init(this)
        startKoin {
            androidLogger()
            androidContext(this@FactCheckerApplication)
            modules(listOf(appModule))
        }
    }
}