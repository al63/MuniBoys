package ride.the.bus.muniboys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ride.the.bus.muniboys.api.NextBusApi
import ride.the.bus.muniboys.models.PredictionsModel

class BusActivity : AppCompatActivity() {

    var mText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        mText = findViewById(R.id.text) as TextView
    }

    override fun onResume() {
        super.onResume()
        NextBusApi.getPredictions(object : Callback<PredictionsModel> {
            override fun onResponse(call: Call<PredictionsModel>?, response: Response<PredictionsModel>?) {
                var time = "dunno"
                response?.let {
                    if (it.isSuccessful) {
                        it.body()?.getNextPrediction()?.let {
                            time = "${it.minutes} ${it.seconds}"
                        }

                    }

                }

                mText?.let {
                    it.text = time
                }
            }

            override fun onFailure(call: Call<PredictionsModel>?, t: Throwable?) {
                mText?.let {
                    it.text = "error!"
                }
            }
        })
    }
}
