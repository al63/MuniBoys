package ride.the.bus.muniboys.api

import io.reactivex.Observable
import io.reactivex.Single
import ride.the.bus.muniboys.models.PredictionsModel
import ride.the.bus.muniboys.models.PredictionsModel2

/**
 * Created by aleclee on 8/6/17.
 */

object NextBusApi {

    private val MUNI_AGENCY = "sf-muni"
    private val mService: NextBusService = RetrofitManager.getRetrofit().create(NextBusService::class.java)

    fun getPredictions(): Single<PredictionsModel2> {
        return mService.getPredictions(MUNI_AGENCY, "5", 5390)
    }
}
