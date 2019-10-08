package com.redbeanlatte11.factchecker.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.redbeanlatte11.factchecker.databinding.DonationFragBinding
import com.redbeanlatte11.factchecker.more.DonationViewModel.Companion.DEFAULT_DONATION_AMOUNT
import com.redbeanlatte11.factchecker.util.getViewModelFactory

class DonationFragment : Fragment() {

    private val viewModel by viewModels<DonationViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: DonationFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DonationFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        setupNumberPicker()
        return viewDataBinding.root
    }

    private fun setupNumberPicker() {
        viewDataBinding.numberPicker.run {
            minValue = 1
            maxValue = 5
            wrapSelectorWheel = false
            setOnValueChangedListener { _, _, new ->
                viewModel.setDonationAmount(new * DEFAULT_DONATION_AMOUNT)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }
}