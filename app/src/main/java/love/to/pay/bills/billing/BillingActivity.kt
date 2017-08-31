package love.to.pay.bills.billing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetailsResponseListener
import love.to.pay.bills.R

class BillingActivity: AppCompatActivity() {

    private var mBillingController: BillingController? = null
    private lateinit var mBuyButton: TextView
    private lateinit var mHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)
        mBuyButton = findViewById(R.id.buy) as TextView
        mHistory = findViewById(R.id.history) as TextView

        mBuyButton.setOnClickListener {
            mBillingController?.launchPurchaseFlow("sku_id_here", BillingClient.SkuType.INAPP)
        }

        mBillingController = BillingController(this, object : BillingController.Listener {
            override fun onHistoryFetched(purchasesResult: Purchase.PurchasesResult) {
                var s = "History \n"
                purchasesResult.purchasesList?.map { purchase ->
                    s += "${purchase.signature} ${purchase.orderId} ${purchase.purchaseState} ${purchase.signature} ${purchase.purchaseToken}"
                }

                mHistory.text = s
            }

            override fun onConnectionChanged(isConnected: Boolean) {
                mBuyButton.visibility = if (isConnected) View.VISIBLE else View.GONE
            }
        })
        mBillingController?.active()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBillingController?.inactive()
    }
}

