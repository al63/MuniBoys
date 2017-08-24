package ride.the.bus.muniboys.models

/**
 * Created by aleclee on 8/6/17.
 */

data class PredictionsModel(val predictions: Predictions) {

    data class Predictions(val direction: List<Direction>, // wtf not necessarily a list
                           val agencyTitle: String,
                           val routeTag: String,
                           val routeTitle: String,
                           val stopTag: String,
                           val stopTitle: String)

    data class Direction(val prediction: List<Prediction>, // wtf not necessarily a list
                         val title: String)

    data class Prediction(val block: String,
                         val dirTag: String,
                         val minutes: String,
                         val seconds: String,
                         val isDeparture: Boolean)


    fun getClosestPrediction(): Prediction? {
        // for each direction...
        // get the first prediction
        // return prediction with smallest minutes

        var bestPrediction: Prediction? = null
        var bestTime = Integer.MAX_VALUE
        for ((prediction) in predictions.direction) {
            val time = prediction.getOrNull(0)?.minutes?.toInt() ?: Integer.MAX_VALUE
            if (bestTime > time) {
                bestTime = time
                bestPrediction = prediction.getOrNull(0)
            }
        }
        return bestPrediction
    }
}

