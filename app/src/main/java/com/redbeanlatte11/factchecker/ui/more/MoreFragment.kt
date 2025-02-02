package com.redbeanlatte11.factchecker.ui.more

import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.redbeanlatte11.factchecker.BuildConfig
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.ui.home.VideosFilterType
import com.redbeanlatte11.factchecker.util.linkToGooglePlay

class MoreFragment : PreferenceFragmentCompat() {

    companion object {

        const val GOOGLE_PLAY_URI = "https://play.google.com/store/apps/details?id=com.redbeanlatte11.factchecker"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.more_preferences, rootKey)
        setHasOptionsMenu(true)

        setupPreferences()
    }

    private fun setupPreferences() {
        val googleAccountPreference: Preference? = findPreference("google_account")
        googleAccountPreference?.setOnPreferenceClickListener {
            val action = MoreFragmentDirections.actionMoreDestToGoogleAccountDest(MoreFragment::class.java.simpleName)
            findNavController().navigate(action)
            true
        }

        val donationPreference: Preference? = findPreference("donation")
        donationPreference?.setOnPreferenceClickListener {
            val action = MoreFragmentDirections.actionMoreDestToDonationDest()
            findNavController().navigate(action)
            true
        }

        val reportedVideosPreference: Preference? = findPreference("reported_videos")
        reportedVideosPreference?.setOnPreferenceClickListener {
            val action = MoreFragmentDirections.actionMoreDestToVideosDest(
                getString(R.string.title_reported_videos),
                VideosFilterType.REPORTED_VIDEOS
            )
            findNavController().navigate(action)
            true
        }

        val excludedVideosPreference: Preference? = findPreference("excluded_videos")
        excludedVideosPreference?.setOnPreferenceClickListener {
            val action = MoreFragmentDirections.actionMoreDestToVideosDest(
                getString(R.string.title_excluded_videos),
                VideosFilterType.EXCLUDED_VIDEOS
            )
            findNavController().navigate(action)
            true
        }

        val reportMessagePreference: EditTextPreference? = findPreference(requireContext().getString(R.string.saved_report_message))
        reportMessagePreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            preference.text
        }

        val commentMessagePreference: EditTextPreference? = findPreference(requireContext().getString(R.string.saved_comment_message))
        commentMessagePreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            preference.text
        }

        val timeoutValuePreference: Preference? = findPreference(getString(R.string.saved_timeout_value))
        timeoutValuePreference?.summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
            "${preference.value}${getString(R.string.seconds)}"
        }

        val appVersionPreference: Preference? = findPreference("app_version")
        appVersionPreference?.title = "${requireContext().getString(R.string.title_app_version)} ${BuildConfig.VERSION_NAME}"
        appVersionPreference?.setOnPreferenceClickListener {
            requireContext().linkToGooglePlay(GOOGLE_PLAY_URI)
            true
        }
    }
}