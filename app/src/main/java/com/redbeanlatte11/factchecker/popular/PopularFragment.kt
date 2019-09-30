package com.redbeanlatte11.factchecker.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.util.getViewModelFactory

class PopularFragment : Fragment() {

    private val viewModel by viewModels<PopularViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.popular_frag, container, false)
        return root
    }
}