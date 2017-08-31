package love.to.pay.bills.billing

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsResponseListener

/**
 * Created by aleclee on 8/30/17.
 */

class BillingController(private val mActivity: Activity,
                        private val mListener: Listener) {

    interface Listener {
       fun onConnectionChanged(isConnected: Boolean)
        fun onHistoryFetched(purchasesResult: Purchase.PurchasesResult)
    }

    private var mTokensToBeConsumed: MutableSet<String> = mutableSetOf()
    private var mIsConnected = false
    private val mPurchaseUpdateListener = PurchasesUpdatedListener { responseCode, purchases ->
        if (responseCode == BillingClient.BillingResponse.OK) {
            handlePurchasesUpdated(purchases)
        }
    }

    private val mBillingClient = BillingClient.Builder(mActivity).setListener(mPurchaseUpdateListener).build()

    fun active() {
        mBillingClient.startConnection(object: BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                mIsConnected = false
                mListener.onConnectionChanged(false)
            }

            override fun onBillingSetupFinished(resultCode: Int) {
                if (resultCode == BillingClient.BillingResponse.OK) {
                    mIsConnected = true
                    mListener.onConnectionChanged(true)

                    // query purchases
                    val inappPurchases = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP)
                    if (mBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS) == BillingClient.BillingResponse.OK) {
                        val subPurchases = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS)

                        if (subPurchases.responseCode == BillingClient.BillingResponse.OK) {
                            inappPurchases.purchasesList.addAll(subPurchases.purchasesList)
                        }
                    }

                    if (inappPurchases.responseCode == BillingClient.BillingResponse.OK) {
                        handlePurchasesUpdated(inappPurchases.purchasesList)
                    }

                    // query purchase history // TODO: can be used to verify with server
                    mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) { result ->
                        if (result.responseCode == BillingClient.BillingResponse.OK) {
                            mListener.onHistoryFetched(result)
                        }
                    }
                }
            }
        })
    }

    fun querySkuDetails(itemType: String, skuList: List<String>, listener: SkuDetailsResponseListener) {
        if (mIsConnected) {
            mBillingClient.querySkuDetailsAsync(itemType, skuList, listener)
        }
    }

    fun launchPurchaseFlow(skuId: String, @BillingClient.SkuType skuType: String) {
        if (mIsConnected) {
            val billingFlowParams = BillingFlowParams.Builder().setSku(skuId).setType(skuType).setOldSkus(null).build()
            mBillingClient.launchBillingFlow(mActivity, billingFlowParams)
        }
    }

    private fun consume(purchaseToken: String, listener: ConsumeResponseListener) {
        if (mTokensToBeConsumed.contains(purchaseToken)) {
            return
        }

        mTokensToBeConsumed.add(purchaseToken)
        if (mIsConnected) {
            mBillingClient.consumeAsync(purchaseToken, listener)
        }
    }

    private fun handlePurchasesUpdated(purchaseList: List<Purchase>) {
        // TODO: am i actually supposed to be consuming each time like this?
        purchaseList.forEach {
            consume(it.purchaseToken, ConsumeResponseListener { purchaseToken, resultCode ->
                if (resultCode == BillingClient.BillingResponse.OK) {
                    Log.d("billing", "consumed")
                }
            })
        }

    }


    fun inactive() {
        if (mBillingClient.isReady) {
            mBillingClient.endConnection()
        }
    }
}
