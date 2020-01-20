package com.redbeanlatte11.factchecker.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Video
import timber.log.Timber

fun Context.watchYoutubeVideo(video: Video) {
    Timber.d("watchYoutubeVideo: ${video.snippet.title}")
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${video.id}"))
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(video.youtubeUrl)
    )
    try {
        startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        startActivity(webIntent)
    }
}

fun Context.watchYoutubeChannel(channel: Channel) {
    Timber.d("watchYoutubeChannel: ${channel.snippet.title}")
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(channel.youtubeUrl)
    )
    startActivity(webIntent)
}

fun Context.mute() {
    Timber.d("mute")
    val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
}

fun Context.unmute() {
    Timber.d("unmute")
    val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
}

fun Context.linkToGooglePlay(uri: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(uri)
        setPackage("com.android.vending")
    }
    startActivity(intent)
}
