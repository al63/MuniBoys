package ride.the.bus.muniboys.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ride.the.bus.muniboys.models.PredictionsModel
import ride.the.bus.muniboys.models.PredictionsModel2

/**
 * Created by aleclee on 8/6/17.
 */
interface NextBusService {
    @GET("publicJSONFeed?command=predictions")
    fun getPredictions(@Query("a") agency: String,
                       @Query("r") route: String,
                       @Query("s") stopId: Int): Single<PredictionsModel2>
}
