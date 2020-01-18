package com.redbeanlatte11.factchecker

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.redbeanlatte11.factchecker.data.source.remote.FactCheckerService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FactCheckerServiceTest {

    @Test
    fun getVideo() = runBlocking {
        val factCheckerService = FactCheckerService.create()
        val video = factCheckerService.getVideo("WIwB72XAEAE")
        print(video)
    }
}