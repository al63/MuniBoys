/*
 * Twitch Android App
 * Copyright (c) 2012-2017 Twitch Interactive, Inc.
 */

package ride.the.bus.muniboys.api
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException

class PostProcessTypeAdapterFactory: TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(reader: JsonReader): T {
                val obj = delegate.read(reader)
                when (obj) {
                    is PostProcess -> obj.postProcess()
                }
                return obj
            }
        }
    }
}
