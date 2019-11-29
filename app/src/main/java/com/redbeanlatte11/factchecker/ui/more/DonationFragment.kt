package com.redbeanlatte11.factchecker.ui.more

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redbeanlatte11.factchecker.databinding.DonationFragBinding
import com.redbeanlatte11.factchecker.domain.DonateUseCase.Companion.DEFAULT_DONATION_AMOUNT
import com.redbeanlatte11.factchecker.util.BillingManager
import org.koin.android.viewmodel.ext.android.viewModel

class DonationFragment : Fragment() {

    private val viewModel: DonationViewModel by viewModel()

    private lateinit var viewDataBinding: DonationFragBinding

    private lateinit var billingManager: BillingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DonationFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupNumberPicker()
        setupBillingManager()
    }

    private fun setupBillingManager() {
        billingManager = BillingManager(activity as Activity)
        viewModel.billingManager = billingManager
    }

    private fun setupNumberPicker() {
        viewDataBinding.numberPicker.run {
            minValue = 1
            maxValue = 5
            wrapSelectorWheel = false
            setOnValueChangedListener { _, _, newValue ->
                viewModel.setDonationAmount(newValue * DEFAULT_DONATION_AMOUNT)
            }
        }
    }
}