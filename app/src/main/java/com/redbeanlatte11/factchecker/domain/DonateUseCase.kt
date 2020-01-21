package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.util.BillingManager

class DonateUseCase {

    operator fun invoke(
        billingManager: BillingManager,
        donationAmount: Int,
        onPurchaseFinished: () -> Unit
    ) {
        val skuId: String = when(donationAmount / DEFAULT_DONATION_AMOUNT) {
            1 -> BillingManager.SKU_ID_COFFEE_1
            2 -> BillingManager.SKU_ID_COFFEE_2
            3 -> BillingManager.SKU_ID_COFFEE_3
            4 -> BillingManager.SKU_ID_COFFEE_4
            5 -> BillingManager.SKU_ID_COFFEE_5
            else -> throw NotImplementedError()
        }

        billingManager.purchase(skuId, onPurchaseFinished)
    }

    companion object {
        const val DEFAULT_DONATION_AMOUNT: Int = 2000
    }
}