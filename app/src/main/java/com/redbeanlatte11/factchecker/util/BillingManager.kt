package com.redbeanlatte11.factchecker.util

import android.app.Activity
import com.android.billingclient.api.*
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class BillingManager(
    private val activity: Activity
) : PurchasesUpdatedListener {

    private var skuDetailsMap: ConcurrentMap<String, SkuDetails> = ConcurrentHashMap()

    private var billingClient: BillingClient = BillingClient.newBuilder(activity)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    init {
        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(billingResult: BillingResult?) {
                Timber.d("onBillingSetupFinished")
                getSkuDetailsList()
            }

            override fun onBillingServiceDisconnected() {
                Timber.d("onBillingServiceDisconnected")
            }
        })
    }

    private fun getSkuDetailsList() {
        Timber.d("getSkuDetailsList")
        if (billingClient.isReady) {
            val skuList = listOf(
                SKU_ID_COFFEE_1,
                SKU_ID_COFFEE_2,
                SKU_ID_COFFEE_3,
                SKU_ID_COFFEE_4,
                SKU_ID_COFFEE_5
            )
            val params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()

            billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Timber.d("querySkuDetailsAsync, responseCode: ${billingResult.responseCode}")
                    if (skuDetailsList.isEmpty()) {
                        Timber.w("skuDetailsList is empty")
                        return@querySkuDetailsAsync
                    }
                }
            }
        } else {
            Timber.w("BillingClient not ready")
        }
    }

    fun purchase(skuId: String) {
        Timber.d("purchase: $skuId")
        val skuDetails = skuDetailsMap[skuId]

        skuDetails?.let {
            val billingFlowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(it)
                .build()
            billingClient.launchBillingFlow(activity, billingFlowParams)
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        Timber.d("onPurchasesUpdated: ${billingResult.responseCode}")
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.

            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
//                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener)
            }
        }
    }

    companion object {
        const val SKU_ID_COFFEE_1 = "coffee_1"
        const val SKU_ID_COFFEE_2 = "coffee_2"
        const val SKU_ID_COFFEE_3 = "coffee_3"
        const val SKU_ID_COFFEE_4 = "coffee_4"
        const val SKU_ID_COFFEE_5 = "coffee_5"
    }
}