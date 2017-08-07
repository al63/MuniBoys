package ride.the.bus.muniboys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ride.the.bus.muniboys.api.NextBusApi

class BusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
    }

    override fun onResume() {
        super.onResume()
        NextBusApi.getPredictions(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.d("lol", "omg")
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
            }

        })
    }
}