package com.redbeanlatte11.factchecker.ui.home

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.redbeanlatte11.factchecker.R
import timber.log.Timber

class ReportAllDialogFragment(
    private val targetCount: Int,
    private val perform: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val message = getString(R.string.dialog_report_all_prefix) +
                    " $targetCount " +
                    getString(R.string.dialog_report_all_postfix)

            builder.setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    try {
                        perform()
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