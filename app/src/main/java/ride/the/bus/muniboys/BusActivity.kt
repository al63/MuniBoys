package ride.the.bus.muniboys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ride.the.bus.muniboys.api.NextBusApi
import ride.the.bus.muniboys.models.PredictionsModel
import ride.the.bus.muniboys.models.PredictionsModel2

class BusActivity : AppCompatActivity() {

    var mText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        mText = findViewById(R.id.text) as TextView
    }

    override fun onResume() {
        super.onResume()
        val single = NextBusApi.getPredictions()
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: SingleObserver<PredictionsModel2> {
                    override fun onSuccess(response: PredictionsModel2) {
                        response.getClosestPrediction()?.let { prediction ->
                            mText?.let {
                                it.text = getString(R.string.coming, prediction.minutes)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        mText?.let {
                            it.text = "dunno"
                        }
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }
}
