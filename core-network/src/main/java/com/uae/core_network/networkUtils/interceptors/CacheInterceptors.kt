package com.uae.core_network.networkUtils.interceptors

//import com.uae.core_common.utils.CheckInternetConnectivity
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CacheInterceptors @Inject constructor(@ApplicationContext val applicationContext: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()

            val cache = CacheControl.Builder()
                .maxAge(1, TimeUnit.SECONDS)
                .maxStale(1, TimeUnit.MINUTES)
//                .onlyIfCached()
                .build()

            val newRequest = request.newBuilder()
                .cacheControl(cache)
                .build()

            Log.d("fikgjiffv", "Cache Interceptorrr")
            val isInternet = /*CheckInternetConnectivity.isInternet(applicationContext)*/ true
            Log.d("fikgjiffv", "Cache Interceptorrr $isInternet")

            return if (!isInternet) {

/**If max-stale expired then the normal regular request will be executed not Cache Request*/


                val cacheResponse = chain.proceed(newRequest)

//                Log.d("dlvjkvnkfv", (cacheResponse?.cacheResponse() == null).toString())
/**If cache response is null and internet also not available then this condition will meet.*/

                Log.d("fikgjiffv", "internet connection not available")

                if(cacheResponse.cacheResponse == null){
                    Log.d("fikgjiffv", "Cache Failed")
                }


//                val apiResponseStr = GsonUtil.toJson(apiResponse)

                cacheResponse
            } else {
                chain.proceed(request)
            }
        }catch (e:Exception){
            Log.d("vknvfknvfv",e.message.toString())
            return chain.proceed(chain.request())
        }
    }



}
