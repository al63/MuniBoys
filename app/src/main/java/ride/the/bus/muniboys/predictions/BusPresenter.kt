package ride.the.bus.muniboys.predictions

import android.os.Handler
import android.os.Looper
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ride.the.bus.muniboys.api.NextBusApi
import ride.the.bus.muniboys.models.PredictionsModel

/**
 * Created by aleclee on 8/25/17.
 */

class BusPresenter internal constructor(private val mApi: NextBusApi,
                                        private val mViewDelegate: BusViewDelegate,
                                        private val mHandler: Handler) {

    private var mIsActive: Boolean = false

    companion object {
        fun create(viewDelegate: BusViewDelegate): BusPresenter {
            return BusPresenter(NextBusApi, viewDelegate, Handler(Looper.getMainLooper()))
        }
    }

    fun onResume() {
        mIsActive = true
        requestPredictions()
    }

    fun onPause() {
        mIsActive = false
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun requeueRequest() {
        mHandler.postDelayed({
            if (mIsActive) {
                requestPredictions()
            }
        }, 60 * 1000)
    }

    private fun requestPredictions() {
        mApi.getPredictions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: SingleObserver<PredictionsModel> {
                    override fun onSuccess(response: PredictionsModel) {
                        response.getClosestPredictions(5).map {
                            it.minutes.toInt()
                        }.apply {
                            if (size > 0) {
                                mViewDelegate.showBusComing(this)
                            } else {
                                mViewDelegate.showBusNotComing()
                            }
                        }
                        requeueRequest()
                    }

                    override fun onError(e: Throwable) {
                        requeueRequest()
                        mViewDelegate.showError()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }
}
