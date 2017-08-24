package ride.the.bus.muniboys.api

import io.reactivex.Single
import ride.the.bus.muniboys.models.PredictionsModel

/**
 * Created by aleclee on 8/6/17.
 */

object NextBusApi {

    private val MUNI_AGENCY = "sf-muni"
    private val mService: NextBusService = RetrofitManager.getRetrofit().create(NextBusService::class.java)

    fun getPredictions(): Single<PredictionsModel> {
        return mService.getPredictions(MUNI_AGENCY, "5", 5390)
    }
}
