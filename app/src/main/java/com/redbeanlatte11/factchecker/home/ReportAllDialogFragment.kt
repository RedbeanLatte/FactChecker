package com.redbeanlatte11.factchecker.home

import android.app.Dialog
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.redbeanlatte11.factchecker.R
import timber.log.Timber
import java.lang.Exception

class ReportAllDialogFragment(
    private val viewModel: HomeViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_report_all)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val webView: WebView = activity?.findViewById(R.id.web_view)!!
                    try {
                        viewModel.reportAll(webView)
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