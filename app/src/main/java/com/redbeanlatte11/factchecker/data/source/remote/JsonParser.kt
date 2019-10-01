package com.redbeanlatte11.factchecker.data.source.remote

import android.content.Context
import com.google.gson.Gson
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Video
import org.json.JSONObject

class JsonParser private constructor(
    private val context: Context,
    private val assetPath: String
) {

    private val gson = Gson()
    private var cachedJsonObj: JSONObject? = null

    fun getPopularVideos(): List<Video> {
        cachedJsonObj = cachedJsonObj ?: createJsonObj(context, assetPath)

        val videos = cachedJsonObj?.getJSONArray("items")

        return gson.fromJson(videos.toString(), Array<Video>::class.java)
            .toList()
    }

    fun getChannels(): List<Channel> {
        cachedJsonObj = cachedJsonObj ?: createJsonObj(context, assetPath)

        val channels = cachedJsonObj?.getJSONArray("items")

        return gson.fromJson(channels.toString(), Array<Channel>::class.java)
            .toList()
    }

    private fun createJsonObj(context: Context, assetPath: String): JSONObject {
        val jsonStr = context.assets
            .open(assetPath)
            .bufferedReader()
            .use {
                it.readText()
            }

        return JSONObject(jsonStr)
    }

    companion object {
        fun from(context: Context, assetPath: String) = JsonParser(context, assetPath)
    }
}