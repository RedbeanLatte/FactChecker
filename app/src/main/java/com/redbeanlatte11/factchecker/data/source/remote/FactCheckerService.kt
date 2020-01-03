package com.redbeanlatte11.factchecker.data.source.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Video
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FactCheckerService {

    @GET("videos")
    suspend fun getVideos(): List<Video>

    @GET("popularvideos")
    suspend fun getPopularVideos(): List<Video>

    @GET("videos")
    suspend fun getVideo(@Query("id") id: String): Video

    @POST("candidatevideos")
    suspend fun addBlacklistVideo(
        @Query("video_id") videoId: String,
        @Query("description") description: String
    )

    //TODO: not implemented
    @GET("channels")
    suspend fun getChannels(): List<Channel>

    //TODO: not implemented
    @POST("candidatechannels")
    suspend fun addBlacklistChannel(@Body channel: Channel)

    companion object {

        private const val BASE_URL = "http://10.253.57.208:4500/"

        fun create(): FactCheckerService {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            return retrofit.create(FactCheckerService::class.java)
        }
    }
}