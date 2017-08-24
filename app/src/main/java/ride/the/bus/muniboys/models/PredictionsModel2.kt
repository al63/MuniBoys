package ride.the.bus.muniboys.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ride.the.bus.muniboys.api.GsonManager
import ride.the.bus.muniboys.api.PostProcess

/**
 * Created by aleclee on 8/23/17.
 */
class PredictionsModel2() : PostProcess {
    @SerializedName("predictions") private var mPredictionsWrapper: PredictionsWrapper? = null
    private var mDirections: List<Direction> = emptyList()
    private var mPredictions: MutableMap<Direction, List<Prediction>> = mutableMapOf()

    data class PredictionsWrapper(val direction: JsonElement, // either a Direction or list of Direction
                                  val agencyTitle: String,
                                  val routeTag: String,
                                  val routeTitle: String,
                                  val stopTag: String,
                                  val stopTitle: String)

    data class Direction(val prediction: JsonElement, // either a Prediction or list of Prediction
                         val title: String)

    data class Prediction(val block: String,
                          val dirTag: String,
                          val minutes: String,
                          val seconds: String,
                          val isDeparture: Boolean)

    override fun postProcess() {
        // The muni API is insane and doesn't actually give correct types. So instead we have to
        // take JsonElement's and figure out what the types are after the fact

        mPredictionsWrapper?.let {
            mDirections = if (it.direction.isJsonArray) {
                GsonManager.getGson().fromJson(it.direction, object: TypeToken<List<Direction>>(){}.type)
            } else {
                listOf(GsonManager.getGson().fromJson(it.direction, Direction::class.java))
            }

            mDirections.forEach { direction ->
                mPredictions.put(direction, if (direction.prediction.isJsonArray) {
                    GsonManager.getGson().fromJson(it.direction, object: TypeToken<List<Prediction>>(){}.type)
                } else {
                    listOf(GsonManager.getGson().fromJson(it.direction, Prediction::class.java))
                })
            }
        }
    }

    fun getBestPrediction(): Prediction? {
        var bestPrediction: Prediction? = null
        var bestMinutes: Int = Integer.MAX_VALUE

        mPredictions.values.mapNotNull {
            it.firstOrNull()
        }.forEach { prediction ->
            val minutes = prediction.minutes.toInt()
            if (bestMinutes > minutes) {
                bestMinutes = minutes
                bestPrediction = prediction
            }
        }

        return bestPrediction
    }

}
