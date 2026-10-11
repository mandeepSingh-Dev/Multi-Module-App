package com.uae.core_network.networkUtils.interceptors

import android.content.Context
import android.util.Log
import com.app.core_common.UserManager
import com.app.core_common.utils.DeepLinkHandler
import com.app.core_common.utils.deepLinkHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class UnAuthorizeInterceptor @Inject constructor(
    @ApplicationContext val context: Context,
    private val userManager: UserManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val encodedPath = request.url.encodedPath

        val body = response.body
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED && !encodedPath.contains("/api/v2/client")) {
            try {
//                    if(!MainActivity.isActive) {

                deepLinkHandler?.navigateToLoginScreen()
//                    }
                CoroutineScope(Dispatchers.IO).launch {
                    userManager.clearLocalData()
                }
//                    context.showToast(message = "Unauthorized user! Please login again.")
            } catch (e: Exception) {
            }
            Log.d("okhttp", "401 UnAuthorized Error")
        }

        return response
    }
}