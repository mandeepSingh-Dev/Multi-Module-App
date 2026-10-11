package com.uae.core_network.networkUtils.interceptors

import android.annotation.SuppressLint
import android.content.Context
import com.app.core_common.datastore.DatastoreManager
import com.uae.core_network.networkUtils.ApiConstants
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresExtension
import com.app.core_common.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    @ApplicationContext val context: Context,
    private val userManager: UserManager,
    private val appDatastore: DatastoreManager
) : Interceptor {

    @SuppressLint("HardwareIds")
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val newRequest = chain.request().newBuilder()

        runBlocking {
//            appDatastore.read(DatastoreConstants.USER_DATA,"").first().let {
            //If user token is not empty then we will paas token to header otherwise return from try block
            try {
                val token = userManager.getUserToken()
                if (token.isEmpty() || token.isBlank())
                    return@runBlocking

                Log.i("okhttp", "${ApiConstants.AUTH_TOKEN} ${token}")
                newRequest.addHeader(ApiConstants.DEVICE_TYPE, "a")
                newRequest.addHeader(
                    ApiConstants.DEVICE_ID, "${
                        Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    }"
                )
                newRequest.addHeader(ApiConstants.APP_VERSION, "1.0")
                newRequest.addHeader(ApiConstants.AUTH_TOKEN, "$token")

            } catch (e: Exception) {
                //
            }
        }
//       }

        return chain.proceed(newRequest.build())
    }
}




