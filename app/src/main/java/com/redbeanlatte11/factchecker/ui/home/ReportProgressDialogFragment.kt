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
import com.redbeanlatte11.factchecker.util.mute
import com.redbeanlatte11.factchecker.util.unmute
import timber.log.Timber

class ReportProgressDialogFragment(
    private val itemCount: Int,
    private val onCancelled: () -> Unit
) : DialogFragment() {

    private var currentVideoIndex = 1

    private var textViewProgress: TextView? = null
    private var textViewCurrentItem: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.progress_dialog, null)
            textViewProgress = view.findViewById(R.id.text_view_progress)
            textViewCurrentItem = view.findViewById(R.id.text_view_current_item)

            builder
                .setView(view)
                .setTitle(R.string.title_progress_report_message)
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

        val progress = "1 / $itemCount"
        textViewProgress?.text = progress
        textViewCurrentItem?.text = ""

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun progress(video: Video) {
        Timber.d("progress: [$currentVideoIndex] ${video.snippet.title}")

        val progressMessage = "$currentVideoIndex / $itemCount"
        textViewProgress?.text = progressMessage
        textViewCurrentItem?.text = video.snippet.title

        currentVideoIndex++
    }
}