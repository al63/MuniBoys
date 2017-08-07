package ride.the.bus.muniboys.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET

/**
 * Created by aleclee on 8/6/17.
 */

object NextBusApi {

    private interface NextBusService {
        @GET("publicJSONFeed?command=predictions&a=sf-muni&r=5&s=4229")
        fun getPredictions(): Call<String>
    }

    private val mService: NextBusService

    init {
        mService = RetrofitManager.getRetrofit().create(NextBusService::class.java)
    }

    fun getPredictions(callback: Callback<String>) {
        mService.getPredictions().enqueue(callback)
    }


}
