package com.redbeanlatte11.factchecker

import android.app.Application
import com.redbeanlatte11.factchecker.di.dataModule
import com.redbeanlatte11.factchecker.di.viewModelModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class FactCheckerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        JodaTimeAndroid.init(this)
        startKoin {
            androidLogger()
            androidContext(this@FactCheckerApplication)
            modules(listOf(dataModule, viewModelModule))
        }
    }
}