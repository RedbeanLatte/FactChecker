package com.redbeanlatte11.factchecker.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.domain.DonateUseCase
import com.redbeanlatte11.factchecker.domain.DonateUseCase.Companion.DEFAULT_DONATION_AMOUNT
import com.redbeanlatte11.factchecker.util.BillingManager
import kotlinx.coroutines.launch

class DonationViewModel(
    private val donateUseCase: DonateUseCase
) : ViewModel() {

    private val _donationAmount = MutableLiveData<Int>(DEFAULT_DONATION_AMOUNT)
    val donationAmount: LiveData<Int> = _donationAmount

    var billingManager: BillingManager? = null

    fun donate() {
        billingManager?.let {
            viewModelScope.launch {
                donateUseCase(it, donationAmount.value ?: DEFAULT_DONATION_AMOUNT)
            }
        }
    }

    fun setDonationAmount(donationAmount: Int) {
        _donationAmount.value = donationAmount
    }
}