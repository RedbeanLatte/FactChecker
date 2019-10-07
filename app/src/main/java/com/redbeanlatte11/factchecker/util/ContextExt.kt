package com.redbeanlatte11.factchecker.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Video

fun Context.watchYoutubeVideo(video: Video) {
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
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(channel.youtubeUrl)
    )
    startActivity(webIntent)
}

fun Context.mute() {
    val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
}

fun Context.unmute() {
    val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
}
