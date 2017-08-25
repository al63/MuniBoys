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

class BusActivity: AppCompatActivity() {

    lateinit var mText: TextView

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
                .subscribe(object: SingleObserver<PredictionsModel> {
                    override fun onSuccess(response: PredictionsModel) {
                        response.getClosestPredictions(5).apply {
                            mText.text = if (size > 0) {
                                getString(R.string.coming, joinToString { it.minutes })
                            } else {
                                getString(R.string.not_coming)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        mText.text = getString(R.string.error_coming)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }
}
