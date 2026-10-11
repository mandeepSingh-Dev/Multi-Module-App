package com.uae.core_network.data

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.app.core_common.UserManager
import com.app.core_common.datastore.DatastoreManager
import com.app.core_common.utils.getDeviceId
import com.google.gson.Gson
import com.uae.core_network.data.service.ServerKeyService
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import com.uae.core_network.networkUtils.Headers
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http2.Header
import okio.Buffer


class EncryptionInterceptor @Inject constructor(
    @ApplicationContext val context: Context,
    serverKeyService: ServerKeyService,
    datastoreManager: DatastoreManager,
    val userManager : UserManager
) : Interceptor {


    val serverKeyManager = ServerKeyManager(serverKeyService, datastoreManager)

    val deviceId: String by lazy {
        getDeviceId(context = context)
    }


    private val language: String = "en"

    private val gson = Gson()

    override fun intercept(
        chain: Interceptor.Chain
    ): okhttp3.Response {

        val serverPublicKey = runBlocking { serverKeyManager.getServerPublicKey() }

        val originalRequest = chain.request()

        val originalBody = originalRequest.body

        val clientEncryptionPublicKeyHeaderKey = CryptoManager.getClientEncryptionPublicKey()

        val token = runBlocking {
            userManager.getUserToken()
        }

        Log.i("okhttp.OkHttpClient","${Headers.authorization} $token")
        val isApiForEncryption = EndPoints.apisWithoutEncryption.contains(originalRequest.url.pathSegments.lastOrNull())


        if (originalBody == null || (originalBody.contentLength() == 0L) || isApiForEncryption) {
            val request = originalRequest
                .newBuilder()
                .header("lang", language)
                .header("deviceid", getDeviceId(context))
                .header("deviceType", "android")
                .header(Headers.authorization, token)
                .header("x-client-encryption-key", clientEncryptionPublicKeyHeaderKey)
                .build()

            return chain.proceed(request)
        }


        // --------------------------------------------------------
        // Get original JSON body
        // --------------------------------------------------------

        val buffer = Buffer()
        originalBody.writeTo(buffer)
        val plainJson = buffer.readUtf8()

        Log.d("fkbnkfbnf", plainJson.toString())

        // --------------------------------------------------------
        // Encrypt + sign original JSON
        // --------------------------------------------------------

        val result = CryptoManager.encryptAndSign(
            plainJson = plainJson,
            serverPublicKeyHex = serverPublicKey
        )


        // --------------------------------------------------------
        // Create encrypted request body
        // --------------------------------------------------------



        val encryptedRequest = mapOf(
            "encryptedPayload" to result.encryptedPayload,
            "signature" to result.signature,

            // Send signing public key if backend expects
            // it in the request body.
            "clientSigningPublicKey" to CryptoManager.getClientSigningPublicKey(),
            "clientEncryptionPublicKey" to CryptoManager.getClientEncryptionPublicKey()
        )


        val encryptedJson = gson.toJson(encryptedRequest)

        Log.d("fbknfkbnf", encryptedJson.toString())

        val requestBody =
            encryptedJson.toRequestBody(
                "application/json".toMediaType()
            )


        // --------------------------------------------------------
        // Add backend-required headers
        // --------------------------------------------------------

        val newRequest = originalRequest
            .newBuilder()
            .header("lang", language)
            .header("deviceid", deviceId)
            .header("deviceType", "android")
            .header("Authorization", token)
            .header("x-client-encryption-key", CryptoManager.getClientEncryptionPublicKey())
            .method(originalRequest.method, requestBody)
            .build()

        Log.d("fkbnkfbnf", newRequest.headers.toString())

        return chain.proceed(newRequest)
    }
}

