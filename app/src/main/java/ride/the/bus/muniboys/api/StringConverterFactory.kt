/*
 * Twitch Android App
 * Copyright (c) 2012-2017 Twitch Interactive, Inc.
 */

package ride.the.bus.muniboys.api

import java.lang.reflect.Type

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

class StringConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return when (type) {
            String::class.java -> return Converter<ResponseBody, String> { value -> value.string() }
            else -> null
        }
    }
}
