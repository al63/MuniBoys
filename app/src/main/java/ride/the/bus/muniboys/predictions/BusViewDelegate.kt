package ride.the.bus.muniboys.predictions

import android.content.Context
import android.view.View
import android.widget.TextView
import ride.the.bus.muniboys.R

/**
 * Created by aleclee on 8/25/17.
 */

class BusViewDelegate internal constructor(private val mContext: Context,
                                           private val mRoot: View) {

    companion object {
        fun create(context: Context, root: View): BusViewDelegate {
            return BusViewDelegate(context, root)
        }
    }

    private val mText: TextView = mRoot.findViewById(R.id.text) as TextView

    fun showError() {
        mText.text = mContext.getString(R.string.error_coming)
    }

    fun showBusComing(times: List<Int>) {
        mText.text = mContext.getString(R.string.coming, times.joinToString())
    }

    fun showBusNotComing() {
        mText.text = mContext.getString(R.string.not_coming)
    }

}
