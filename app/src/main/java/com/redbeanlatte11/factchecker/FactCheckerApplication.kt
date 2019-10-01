package com.redbeanlatte11.factchecker

import android.app.Application
import com.redbeanlatte11.factchecker.data.source.VideosRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FactCheckerApplication : Application() {

    val videoRepository: VideosRepository
        get() = ServiceLocator.provideProductsRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
//        JodaTimeAndroid.init(this)
        startKoin {
            androidContext(this@FactCheckerApplication)
            modules(appModule)
        }
    }
}