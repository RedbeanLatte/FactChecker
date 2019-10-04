package com.redbeanlatte11.factchecker.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.redbeanlatte11.factchecker.R
import timber.log.Timber

class PreferenceUtils {

    companion object {
        fun loadSignInResult(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val signInResult = sharedPreferences.getBoolean(
                context.getString(R.string.saved_sign_in_result),
                false
            )
            Timber.d("loadSignInResult: $signInResult")
            return signInResult
        }

        fun saveSignInResult(context: Context, signInResult: Boolean) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharedPref.edit()) {
                putBoolean(
                    context.getString(R.string.saved_sign_in_result),
                    signInResult
                )
                commit()
            }
            Timber.d("saveSignInResult: $signInResult")
        }

        fun loadReportMessage(context: Context): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val defaultValue = context.getString(R.string.default_report_message)
            val reportMessage = sharedPreferences.getString(
                context.getString(R.string.saved_report_message),
                defaultValue
            )
            Timber.d("loadReportMessage: $reportMessage")
            return reportMessage ?: defaultValue
        }

        fun saveReportMessage(context: Context, reportMessage: String) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharedPreferences.edit()) {
                putString(
                    context.getString(R.string.saved_report_message),
                    reportMessage
                )
                commit()
            }
            Timber.d("saveReportMessage: $reportMessage")
        }
    }
}
