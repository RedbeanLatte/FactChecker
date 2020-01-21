package com.redbeanlatte11.factchecker.ui.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redbeanlatte11.factchecker.Event
import com.redbeanlatte11.factchecker.R
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

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _donationFinishedEvent = MutableLiveData<Event<Unit>>()
    val donationFinishedEvent: LiveData<Event<Unit>> = _donationFinishedEvent

    fun donate() {
        billingManager?.let {
            viewModelScope.launch {
                donateUseCase(it, donationAmount.value ?: DEFAULT_DONATION_AMOUNT) {
                    showSnackbarMessage(R.string.thanks_to_donation_message)
                    _donationFinishedEvent.value = Event(Unit)
                }
            }
        }
    }

    fun setDonationAmount(donationAmount: Int) {
        _donationAmount.value = donationAmount
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }
}