package com.redbeanlatte11.factchecker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.redbeanlatte11.factchecker.databinding.AddBlacklistVideoFragBinding
import com.redbeanlatte11.factchecker.util.setupSnackbar
import org.koin.android.viewmodel.ext.android.viewModel

class AddBlacklistVideoFragment : Fragment() {

    private lateinit var viewDataBinding: AddBlacklistVideoFragBinding

    private val args: AddBlacklistVideoFragmentArgs by navArgs()

    private val viewModel: AddBlacklistVideoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = AddBlacklistVideoFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setVideoUrl(args.videoUrl)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}