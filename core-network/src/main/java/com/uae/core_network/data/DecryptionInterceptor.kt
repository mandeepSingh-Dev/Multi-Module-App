package com.uae.core_network.data

import android.util.Log
import com.app.core_common.datastore.DatastoreManager
import com.app.core_common.utils.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.uae.core_network.data.service.ServerKeyService
import com.uae.core_network.models.ApiResponse
import com.uae.core_network.models.ApiResponse2
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.ByteString.Companion.encode
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

class DecryptionInterceptor @Inject constructor(
    private val serverKeyService: ServerKeyService,
    private val datastoreManager: DatastoreManager
) : Interceptor {

    private val gson = Gson()

/*    override fun intercept(
        chain: Interceptor.Chain
    ): okhttp3.Response {

        val serverKeyManager = ServerKeyManager(serverKeyService, datastoreManager)

        val serverPublicKey = runBlocking { serverKeyManager.getServerPublicKey() }


        val response = chain.proceed(chain.request())
        // Read response body.
        val responseBody = response.body ?: return response

        val encryptedJson = responseBody.string()

        // Parse:
        //
        // {
        //   "encryptedPayload": "...",
        //   "signature": "..."
        // }


        //If No output from

        if (encryptedJson.isEmpty()) {
            Log.d("fklbnkfbnf", "0")
            return chain.proceed(chain.request())
        }
        try {
            val responseObject = JSONObject(encryptedJson)

            val hasDataKeyObject = responseObject.has("data")

            if (hasDataKeyObject && response.isSuccessful) {
                val dataKey = responseObject.get("data")
                if (dataKey is String) {
                    val encryptedResponse = encryptedJson.fromJson<ApiResponse>()

                    // Decrypt encryptedPayload.
                    val decryptedJson = CryptoManager.decryptPayload(
                        encryptedPayload = encryptedResponse?.data ?: "",
                        serverPublicKeyHex = serverPublicKey
                    )


                    Log.d("fkbfkbnfk", decryptedJson)
                    // Create a new response body containing
                    // the original JSON.


                    val decryptedDataObj = decryptedJson.normalizeDecryptedJsonToJsonObject()
//            responseObject.put("data", decryptedData)

                    responseObject.put("data", decryptedDataObj)

                    val newBody =
                        responseObject.toString().toResponseBody(responseBody.contentType())

                    return response
                        .newBuilder()
                        .body(newBody)
                        .build()
                } else {
                    Log.d("fklbnkfbnf", "1")
                    return chain.proceed(chain.request())
                }
            } else {
                Log.d("fklbnkfbnf", "2")
                return chain.proceed(chain.request())
            }
        }catch (e: Exception){
            Log.d("fklbnkfbnf", "3")
            return chain.proceed(chain.request())
        }
    }*/

    override fun intercept(
        chain: Interceptor.Chain
    ): okhttp3.Response {

        val serverKeyManager = ServerKeyManager(serverKeyService, datastoreManager)

        val serverPublicKey = runBlocking { serverKeyManager.getServerPublicKey() }


        // Execute API ONLY ONCE
        val response = chain.proceed(chain.request())

        val responseBody = response.body
            ?: return response

        val responseContentType = responseBody.contentType()

        // Reading string consumes the original body
        val encryptedJson = responseBody.string()

        if (encryptedJson.isEmpty()) {
            return response.newBuilder()
                .body(
                    "".toResponseBody(responseContentType)
                )
                .build()
        }

        return try {

            val responseObject = JSONObject(encryptedJson)

            val hasDataKeyObject =
                responseObject.has("data")

            if (hasDataKeyObject && response.isSuccessful) {

                val dataKey = responseObject.get("data")

                if (dataKey is String) {

                    val encryptedResponse =
                        encryptedJson.fromJson<ApiResponse>()

                    val decryptedJson =
                        CryptoManager.decryptPayload(
                            encryptedPayload = encryptedResponse?.data ?: "",
                            serverPublicKeyHex = serverPublicKey
                        )

                    Log.d(
                        "DecryptionInterceptor",
                        "Decrypted: $decryptedJson"
                    )

                    val decryptedDataObj =
                        decryptedJson.normalizeDecryptedJsonToJsonObject()

                    responseObject.put(
                        "data",
                        decryptedDataObj
                    )

                    val newBody =
                        responseObject
                            .toString()
                            .toResponseBody(responseContentType)

                    response.newBuilder()
                        .body(newBody)
                        .build()

                } else {

                    // Don't call chain.proceed()
                    response.newBuilder()
                        .body(
                            encryptedJson.toResponseBody(
                                responseContentType
                            )
                        )
                        .build()
                }

            } else {

                // Don't call chain.proceed()
                response.newBuilder()
                    .body(
                        encryptedJson.toResponseBody(
                            responseContentType
                        )
                    )
                    .build()
            }

        } catch (e: Exception) {

            Log.e(
                "DecryptionInterceptor",
                "Decryption failed",
                e
            )

            // Don't call chain.proceed()
            response.newBuilder()
                .body(
                    encryptedJson.toResponseBody(
                        responseContentType
                    )
                )
                .build()
        }
    }
}

fun String?.normalizeDecryptedJsonToJsonObject(): Any? {
    if (this.isNullOrEmpty()) return null
    val trimmedJson = this.trim()

    return when {
        trimmedJson.startsWith("{") -> {
            JSONObject(trimmedJson)
        }

        trimmedJson.startsWith("[") -> {
            JSONArray(trimmedJson)
        }

        else -> {
            trimmedJson
        }
    }

}