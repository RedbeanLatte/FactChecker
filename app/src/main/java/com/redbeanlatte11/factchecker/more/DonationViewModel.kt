package com.redbeanlatte11.factchecker.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.domain.DonateUseCase
import kotlinx.coroutines.launch

class DonationViewModel(
    private val donateUseCase: DonateUseCase
) : ViewModel() {

    private val _donationAmount = MutableLiveData<Int>(DEFAULT_DONATION_AMOUNT)
    val donationAmount: LiveData<Int> = _donationAmount

    fun setDonationAmount(donationAmount: Int) {
        _donationAmount.value = donationAmount
    }

    fun donate() {
        viewModelScope.launch {
            donateUseCase(donationAmount.value ?: DEFAULT_DONATION_AMOUNT)
        }
    }

    companion object {
        const val DEFAULT_DONATION_AMOUNT: Int = 2000
    }
}