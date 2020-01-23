package com.redbeanlatte11.factchecker.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.ui.home.SearchPeriod
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

        fun loadCommentMessage(context: Context): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val defaultValue = context.getString(R.string.default_comment_message)
            val reportMessage = sharedPreferences.getString(
                context.getString(R.string.saved_comment_message),
                defaultValue
            )
            Timber.d("loadCommentMessage: $reportMessage")
            return reportMessage ?: defaultValue
        }

        fun loadSearchPeriod(context: Context): SearchPeriod {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val searchPeriod = sharedPreferences.getString(
                context.getString(R.string.saved_search_period),
                SearchPeriod.ALL.toString()
            )
            Timber.d("loadSearchPeriod: $searchPeriod")
            return SearchPeriod.valueOf(searchPeriod!!)
        }

        fun saveSearchPeriod(context: Context, searchPeriod: SearchPeriod) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharedPref.edit()) {
                putString(
                    context.getString(R.string.saved_search_period),
                    searchPeriod.toString()
                )
                commit()
            }
            Timber.d("saveSearchPeriod: $searchPeriod")
        }

        fun loadIsAutoCommentEnabled(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val signInResult = sharedPreferences.getBoolean(
                context.getString(R.string.saved_is_auto_comment_enabled),
                false
            )
            Timber.d("loadIsAutoCommentEnabled: $signInResult")
            return signInResult
        }
    }
}
