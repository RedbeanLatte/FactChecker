package com.redbeanlatte11.factchecker.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import timber.log.Timber

class ReportProgressDialogFragment(
    private val itemCount: Int,
    private val onCancelled: () -> Unit
) : DialogFragment() {

    private var currentVideoIndex = 1

    private var textViewProgress: TextView? = null
    private var textViewCurrentItem: TextView? = null

    private var firstItemTitle: String? = null

    private var progressPostfix: String = ""
    private var reportedVideoCountPostfix: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.progress_dialog, null)
            textViewProgress = view.findViewById(R.id.text_view_progress)
            textViewCurrentItem = view.findViewById(R.id.text_view_current_item)
            progressPostfix = requireActivity().getString(R.string.progress_postfix)
            reportedVideoCountPostfix =
                requireActivity().getString(R.string.reported_video_count_postfix)

            val title = if (PreferenceUtils.loadAutoCommentEnabled(requireContext())) {
                R.string.title_progress_report_and_input_comment
            } else {
                R.string.title_progress_report
            }

            builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton(R.string.cancel) { _, _ ->
                    onCancelled()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        val progressMessage = "1 / $itemCount $progressPostfix"
        textViewProgress?.text = progressMessage
        textViewCurrentItem?.text = firstItemTitle ?: ""

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun progress(video: Video, reportedVideoCount: Int) {
        Timber.d("progress: [$currentVideoIndex] ${video.snippet.title}")

        if (firstItemTitle == null) {
            firstItemTitle = video.snippet.title
        }
        val progressMessage =
            "$currentVideoIndex / $itemCount $progressPostfix ($reportedVideoCount $reportedVideoCountPostfix)"

        textViewProgress?.text = progressMessage
        textViewCurrentItem?.text = video.snippet.title

        currentVideoIndex++
    }
}