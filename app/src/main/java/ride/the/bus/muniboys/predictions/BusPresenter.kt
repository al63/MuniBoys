package ride.the.bus.muniboys.predictions

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ride.the.bus.muniboys.api.NextBusApi
import ride.the.bus.muniboys.models.PredictionsModel

/**
 * Created by aleclee on 8/25/17.
 */

class BusPresenter internal constructor(val mApi: NextBusApi,
                                        val mViewDelegate: BusViewDelegate) {

    companion object {
        fun create(viewDelegate: BusViewDelegate): BusPresenter {
            return BusPresenter(NextBusApi, viewDelegate)
        }
    }

    fun onResume() {
        val single = mApi.getPredictions()
        single.subscribeOn(Schedulers.io())
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
                    }

                    override fun onError(e: Throwable) {
                        mViewDelegate.showError()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }
}
