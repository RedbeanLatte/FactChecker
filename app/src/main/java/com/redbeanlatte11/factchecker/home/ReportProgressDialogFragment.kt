package com.redbeanlatte11.factchecker.home

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

class ReportProgressDialogFragment(
    private val viewModel: VideosViewModel
) : DialogFragment() {

    private val itemCount = viewModel.items.value!!.count()
    private var reportedVideoCount = 0

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
                    viewModel.cancelReportAll()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val progress = "0 / $itemCount"
        textViewProgress?.text = progress
        textViewCurrentItem?.text = viewModel.items.value?.first()?.snippet?.title

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun progress(video: Video) {
        val progressMessage = "$reportedVideoCount / $itemCount"
        textViewProgress?.text = progressMessage
        textViewCurrentItem?.text = video.snippet.title

        reportedVideoCount++
    }

    fun complete() {
        val completeDialog = ReportCompleteDialogFragment(viewModel.items.value?.count()!!)
        completeDialog.show(activity?.supportFragmentManager!!, "ReportCompleteDialogFragment")
        dismiss()
    }
}