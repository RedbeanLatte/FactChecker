package com.redbeanlatte11.factchecker.util

import androidx.appcompat.widget.PopupMenu
import timber.log.Timber

fun PopupMenu.setForceShowIcon(forceShow: Boolean) {
    try {
        val fieldMPopup = androidx.appcompat.widget.PopupMenu::class.java.getDeclaredField("mPopup")
        fieldMPopup.isAccessible = true
        val mPopup = fieldMPopup.get(this)
        mPopup.javaClass
            .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(mPopup, forceShow)
    } catch (e: Exception) {
        Timber.e(e)
    }
}