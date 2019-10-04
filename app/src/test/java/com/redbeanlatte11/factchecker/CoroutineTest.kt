package com.redbeanlatte11.factchecker

import kotlinx.coroutines.*
import org.junit.Test

class CoroutineTest {

    var job: Job? = null

    @Test
    fun testJobCancel() {
        GlobalScope.launch {
            job = GlobalScope.launch {
                repeat(10) {
                    println("hello")
                    delay(500)
                }
            }

            delay(2000)

        }

        Thread.sleep(100)
        job?.cancel()

        Thread.sleep(6000)
    }
}
