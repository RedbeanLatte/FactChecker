package com.redbeanlatte11.factchecker

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.redbeanlatte11.factchecker.data.source.remote.JsonParser
import org.junit.Test

class JsonParserTest {
    @Test
    fun printVideos() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val videos = JsonParser.from(context, "popular_videos.json").getPopularVideos()
        Log.d("JsonParserTest", videos.toString())

        val channels = JsonParser.from(context, "channels.json").getChannels()
        Log.d("JsonParserTest", channels.toString())
    }
}