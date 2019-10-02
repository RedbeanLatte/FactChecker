package com.redbeanlatte11.factchecker.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.watchYoutubeVideo(id: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://m.youtube.com/watch?v=$id")
    )
    try {
        startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        startActivity(webIntent)
    }
}

fun Context.watchYoutubeChannel(id: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://user/channel/$id"))
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://m.youtube.com/channel/$id")
    )
    try {
        startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        startActivity(webIntent)
    }
}