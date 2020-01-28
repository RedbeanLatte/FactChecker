package com.redbeanlatte11.factchecker.ui.home

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.redbeanlatte11.factchecker.R

class SignInDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_sign_in)
                .setPositiveButton(R.string.sign_in) { _, _ ->
                    val action = HomeFragmentDirections.actionHomeDestToGoogleAccountDest(HomeFragment::class.java.simpleName)
                    findNavController().navigate(action)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}