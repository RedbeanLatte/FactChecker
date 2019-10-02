package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.GoogleAccountFragBinding
import com.redbeanlatte11.factchecker.util.getViewModelFactory

class GoogleAccountFragment : Fragment() {

    private val viewModel by viewModels<GoogleAccountViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.google_account_frag, container, false)

        val viewDataBinding = GoogleAccountFragBinding.bind(view)
        viewModel.signIn(viewDataBinding.webView)
        return view
    }
}