package com.redbeanlatte11.factchecker

import com.redbeanlatte11.factchecker.util.YoutubeUrlUtils
import org.junit.Test

class ConfirmUrlTest {

    @Test
    fun testConfirmUrl() {

        println(YoutubeUrlUtils.validateVideoUrl("https://www.youtube.com/watch?v=QiOEQ4xQSO8"))
        println(YoutubeUrlUtils.validateVideoUrl("https://youtu.be/QiOEQ4xQSO8"))
        println(YoutubeUrlUtils.validateVideoUrl("https://www.youtube.com/watch?v=TfVS71BuGbY&list=PLzUSBUvdIZrUs01zl2dB-mF7fLIL-8KSK"))

        println(YoutubeUrlUtils.extractVideoIdFromUrl("https://www.youtube.com/watch?v=TfVS71BuGbY&list=PLzUSBUvdIZrUs01zl2dB-mF7fLIL-8KSK"))
    }
}