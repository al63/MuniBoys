package ride.the.bus.muniboys.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsResponseListener

/**
 * Created by aleclee on 8/30/17.
 */

class BillingController(activity: Activity,
                        private val mListener: Listener) {

    interface Listener {
       fun onConnectionChanged(isConnected: Boolean)
        fun onPurchasesUpdated(purchaseList: List<Purchase>)
    }

    private var mIsConnected = false
    private val mPurchaseUpdateListener = PurchasesUpdatedListener { responseCode, purchases ->

    }

    private val mBillingClient = BillingClient.Builder(activity).setListener(mPurchaseUpdateListener).build()

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
                    val subPurchases = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS)

                    if (subPurchases.responseCode == BillingClient.BillingResponse.OK) {
                        inappPurchases.purchasesList.addAll(subPurchases.purchasesList)
                    }

                    if (inappPurchases.responseCode == BillingClient.BillingResponse.OK) {
                        mListener.onPurchasesUpdated(inappPurchases.purchasesList)
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

    fun inactive() {
        if (mBillingClient.isReady) {
            mBillingClient.endConnection()
        }
    }
}
