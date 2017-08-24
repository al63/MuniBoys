package ride.the.bus.muniboys.models

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ride.the.bus.muniboys.api.GsonManager
import ride.the.bus.muniboys.api.PostProcess

/**
 * Created by aleclee on 8/23/17.
 */
class PredictionsModel(private val mGson: Gson): PostProcess {

    constructor(): this(GsonManager.getGson())

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
        // take JsonElement's and figure out what the types are after the fact.
        // direction and prediction are both either lists or objects.

        mPredictionsWrapper?.direction?.let { direction ->
            // convert direction from JsonElement to real list
            mDirections = if (direction.isJsonArray) {
                mGson.fromJson(direction, object: TypeToken<List<Direction>>() {}.type)
            } else {
                listOf(mGson.fromJson(direction, Direction::class.java))
            }
        }

        // convert directions list to predictions
        mDirections.forEach { direction ->
            mPredictions.put(direction, if (direction.prediction.isJsonArray) {
                mGson.fromJson(direction.prediction, object: TypeToken<List<Prediction>>(){}.type)
            } else {
                listOf(mGson.fromJson(direction.prediction, Prediction::class.java))
            })
        }
    }

    fun getClosestPrediction(): Prediction? {
        return mPredictions.values.mapNotNull {
            it.firstOrNull()
        }.minBy {
            it.minutes
        }
    }
}
