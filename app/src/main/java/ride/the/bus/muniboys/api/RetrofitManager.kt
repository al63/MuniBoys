package ride.the.bus.muniboys.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by aleclee on 8/6/17.
 */

object RetrofitManager {

    private val mRetrofit: Retrofit

    init {
        val client: OkHttpClient = OkHttpClient.Builder()
                .build()

        mRetrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://webservices.nextbus.com/service/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonManager.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun getRetrofit(): Retrofit {
        return mRetrofit
    }
}