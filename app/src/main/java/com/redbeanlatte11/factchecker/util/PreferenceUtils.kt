package com.redbeanlatte11.factchecker.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.ui.channel.ChannelsViewType
import com.redbeanlatte11.factchecker.ui.home.SearchPeriod
import org.joda.time.DateTime
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
            val reportMessage = sharedPreferences.getString(
                context.getString(R.string.saved_report_message),
                ""
            )
            Timber.d("loadReportMessage: $reportMessage")
            return reportMessage ?: ""
        }

        fun saveReportMessage(context: Context, reportMessage: String) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharedPref.edit()) {
                putString(
                    context.getString(R.string.saved_report_message),
                    reportMessage
                )
                commit()
            }
            Timber.d("saveReportMessage: $reportMessage")
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

        fun loadChannelsViewType(context: Context): ChannelsViewType {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val channelsViewType = sharedPreferences.getString(
                context.getString(R.string.saved_channels_view_type),
                ChannelsViewType.DEFAULT_VALUE.toString()
            )
            Timber.d("loadChannelsViewType: $channelsViewType")
            return ChannelsViewType.valueOf(channelsViewType!!)
        }

        fun saveChannelsViewType(context: Context, channelsViewType: ChannelsViewType) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharedPref.edit()) {
                putString(
                    context.getString(R.string.saved_channels_view_type),
                    channelsViewType.toString()
                )
                commit()
            }
            Timber.d("saveChannelsViewType: $channelsViewType")
        }

        fun loadTimeoutValue(context: Context): Int {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val timeoutValue = sharedPreferences.getString(
                context.getString(R.string.saved_timeout_value),
                context.getString(R.string.default_timeout_value)
            )
            Timber.d("loadTimeoutValue: $timeoutValue")
            return timeoutValue?.toInt()!!
        }

        fun saveReportSucceedAt(context: Context, reportCompletedAt: DateTime) {
            val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharePref.edit()) {
                putString(
                    context.getString(R.string.saved_report_succeed_at),
                    reportCompletedAt.toString()
                )
                commit()
            }
            Timber.d("saveReportCompletedAt: $reportCompletedAt")
        }

        fun loadReportSucceedAt(context: Context): DateTime? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val reportSucceedAt = sharedPreferences.getString(
                context.getString(R.string.saved_report_succeed_at),
                null
            )

            return if (reportSucceedAt != null) {
                DateTime.parse(reportSucceedAt)
            } else {
                null
            }
        }
    }
}
