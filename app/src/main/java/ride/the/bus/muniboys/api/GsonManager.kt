package ride.the.bus.muniboys.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder

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