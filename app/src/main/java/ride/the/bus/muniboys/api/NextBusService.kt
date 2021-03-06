package ride.the.bus.muniboys.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ride.the.bus.muniboys.models.PredictionsModel

/**
 * Created by aleclee on 8/6/17.
 */
interface NextBusService {
    @GET("service/publicJSONFeed?command=predictions")
    fun getPredictions(@Query("a") agency: String,
                       @Query("r") route: String,
                       @Query("s") stopId: Int): Single<PredictionsModel>
}
