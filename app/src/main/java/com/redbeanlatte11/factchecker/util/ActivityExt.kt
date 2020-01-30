package com.redbeanlatte11.factchecker.util

import android.app.Activity
import android.view.WindowManager

fun Activity.keepScreenOn() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun Activity.clearKeepScreenOn() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}