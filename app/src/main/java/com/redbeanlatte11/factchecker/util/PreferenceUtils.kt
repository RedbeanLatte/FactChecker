package com.redbeanlatte11.factchecker.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.redbeanlatte11.factchecker.R
import timber.log.Timber

class PreferenceUtils {

    companion object {
        fun loadSignInResult(context: Context): Boolean {
            val defaultValue = false
            val sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context) ?: return defaultValue
            val signInResult = sharedPref.getBoolean(
                context.getString(R.string.saved_sign_in_result),
                defaultValue
            )
            Timber.d("loadSignInResult: $signInResult")
            return signInResult
        }

        fun saveSignInResult(context: Context, signInResult: Boolean) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context) ?: return
            with(sharedPref.edit()) {
                putBoolean(
                    context.getString(R.string.saved_sign_in_result),
                    signInResult
                )
                commit()
            }
            Timber.d("saveSignInResult: $signInResult")
        }
    }
}
