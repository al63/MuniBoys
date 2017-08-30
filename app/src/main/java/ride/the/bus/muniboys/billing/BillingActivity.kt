package ride.the.bus.muniboys.billing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsResponseListener
import ride.the.bus.muniboys.R

class BillingActivity: AppCompatActivity() {


    private var mBillingController: BillingController? = null
    private var mBillingClient: BillingClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)
        mBillingController = BillingController(this, object: BillingController.Listener {
            override fun onPurchasesUpdated(purchaseList: List<Purchase>) {
                Log.d("billing", "purchases updated")
            }

            override fun onConnectionChanged(isConnected: Boolean) {
                if (isConnected) {
                    mBillingController?.querySkuDetails(BillingClient.SkuType.INAPP, listOf("loerl"), mSkuDetailsListener)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mBillingController?.active()
    }

    override fun onPause() {
        super.onPause()
        mBillingController?.inactive()
    }

    private var mSkuDetailsListener = SkuDetailsResponseListener { result ->
        result.skuDetailsList.forEach {  skuDetail ->
            // TODO
        }

    }
}

