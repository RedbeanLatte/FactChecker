package com.redbeanlatte11.factchecker.ui.home

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.redbeanlatte11.factchecker.R

class MessageDialogFragment(
    private val message: String
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}