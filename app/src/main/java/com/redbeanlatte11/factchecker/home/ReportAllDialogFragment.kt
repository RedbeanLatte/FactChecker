package com.redbeanlatte11.factchecker.home

import android.app.Dialog
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.data.Video
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import timber.log.Timber
import java.lang.Exception

class ReportAllDialogFragment(
    private val viewModel: VideosViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_report_all)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val webView: WebView = activity?.findViewById(R.id.web_view)!!
                    try {
                        val progressDialog = ReportProgressDialogFragment(viewModel)
                        progressDialog.isCancelable = false
                        progressDialog.show(activity?.supportFragmentManager!!, "ReportProgressDialogFragment")

                        val listener = object : VideosViewModel.OnReportAllListener {

                            override fun onNext(video: Video) {
                                progressDialog.progress(video)
                            }

                            override fun onCompleted() {
                                progressDialog.complete()
                            }
                        }

                        viewModel.reportAll(
                            webView,
                            PreferenceUtils.loadReportMessage(requireContext()),
                            listener
                        )
                    } catch (e: Exception) {
                        Timber.w("reportAll failed")
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}