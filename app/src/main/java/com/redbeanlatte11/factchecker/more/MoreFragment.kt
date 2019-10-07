package com.redbeanlatte11.factchecker.more

import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.home.VideosFilterType

class MoreFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.more_preferences, rootKey)
        setHasOptionsMenu(true)

        setupPreferences()
    }

    private fun setupPreferences() {
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
            val text = preference.text
            if (TextUtils.isEmpty(text)) {
                requireContext().getString(R.string.default_report_message)
            } else {
                text
            }
        }
    }
}