package ride.the.bus.muniboys.models

/**
 * Created by aleclee on 8/6/17.
 */

data class PredictionsModel(val predictions: Predictions) {

    data class Predictions(val direction: Direction,
                           val agencyTitle: String,
                           val routeTag: String,
                           val routeTitle: String,
                           val stopTag: String,
                           val stopTitle: String)

    data class Direction(val prediction: List<Prediction>,
                         val title: String)

    data class Prediction(val block: String,
                         val dirTag: String,
                         val minutes: String,
                         val seconds: String,
                         val isDeparture: Boolean)

}

