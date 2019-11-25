package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.redbeanlatte11.factchecker.databinding.AddVideoBlacklistFragBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AddVideoBlacklistFragment : Fragment() {

    private lateinit var viewDataBinding: AddVideoBlacklistFragBinding

    private val args: AddVideoBlacklistFragmentArgs by navArgs()

    private val viewModel: AddVideoBlacklistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = AddVideoBlacklistFragBinding.inflate(inflater, container, false).apply {
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
    }
}