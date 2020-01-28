package com.redbeanlatte11.factchecker.ui.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.EventObserver
import com.redbeanlatte11.factchecker.databinding.AddBlacklistChannelFragBinding
import com.redbeanlatte11.factchecker.util.setupSnackbar
import org.koin.android.viewmodel.ext.android.viewModel

class AddBlacklistChannelFragment : Fragment() {

    private lateinit var viewDataBinding: AddBlacklistChannelFragBinding

    private val viewModel: AddBlacklistChannelViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = AddBlacklistChannelFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupNavigation()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        viewModel.blacklistAddedEvent.observe(this, EventObserver {
            findNavController().navigateUp()
        })
    }
}