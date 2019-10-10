package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.redbeanlatte11.factchecker.databinding.AddVideoBlacklistFragBinding
import com.redbeanlatte11.factchecker.util.getViewModelFactory

class AddVideoBlacklistFragment : Fragment() {
    private val viewModel by viewModels<AddVideoBlacklistViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: AddVideoBlacklistFragBinding

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
}