package ride.the.bus.muniboys.singletons

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ride.the.bus.muniboys.api.PostProcessTypeAdapterFactory

/**
 * Created by aleclee on 8/23/17.
 */


object GsonManager {

    private val mGson: Gson = GsonBuilder()
            .registerTypeAdapterFactory(PostProcessTypeAdapterFactory())
            .create()

    fun getGson(): Gson {
        return mGson
    }
}