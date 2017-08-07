package ride.the.bus.muniboys.api

import retrofit2.Callback
import ride.the.bus.muniboys.models.PredictionsModel

/**
 * Created by aleclee on 8/6/17.
 */

object NextBusApi {

    private val MUNI_AGENCY = "sf-muni"
    private val mService: NextBusService = RetrofitManager.getRetrofit().create(NextBusService::class.java)

    fun getPredictions(callback: Callback<PredictionsModel>) {
        mService.getPredictions(MUNI_AGENCY, "5", 5390).enqueue(callback)
    }
}
