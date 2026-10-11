/*
package com.uae.core_network

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import timber.log.Timber
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AESCryptoInterceptor @Inject constructor(
    private val cryptLib: AES,
    private val context: Context
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
        val request = chain.request()

        val plainQueryParameters = request.url.queryParameterNames
        var httpUrl = request.url

        // Check Query Parameters and encrypt
        if (plainQueryParameters.isNotEmpty()) {
            val httpUrlBuilder = httpUrl.newBuilder()
            for (i in plainQueryParameters.indices) {
                val name = httpUrl.queryParameterName(i)
                val value = httpUrl.queryParameterValue(i)
                val enc = cryptLib.encrypt(value)
                httpUrlBuilder.setQueryParameter(name, enc)
            }
            httpUrl = httpUrlBuilder.build()
        }

        // Get Header for encryption
        val apiKey = request.headers[Session.API_KEY]
        val token = request.headers[Session.USER_SESSION]
        val language = request.headers[Session.LANGUAGE]
        val newRequest: Request
        val requestBuilder = request.newBuilder()

        val encryptedApiKey = cryptLib.encrypt(apiKey ?: "")
        Timber.i("ENCRYPTED API KEY: %s", encryptedApiKey)
        val encryptedToken = cryptLib.encrypt(token ?: "")
        Timber.i("ENCRYPTED TOKEN::: %s", encryptedToken)

        // Check if any body and encrypt
        val requestBody = request.body
        Log.d("mvkmfkvf",requestBody.toString())
        if (requestBody?.contentType() != null) {
            // bypass multipart parameters for encryption
            val isMultipart = requestBody.contentType()!!.type.equals("multipart", ignoreCase = true)
            val bodyPlainText = if (isMultipart) bodyToString(requestBody) else cryptLib.encrypt(bodyToString(requestBody))
            if (!isMultipart) {
                requestBuilder.post(
                    bodyPlainText!!.toRequestBody(
                        "text/plain".toMediaTypeOrNull(),
                        0,
                        bodyPlainText.size
                    )
                )
                Timber.i("ENCRYPTED BODY:::: %s", String(bodyPlainText!!, Charset.forName("UTF-8")))
            } else {
                requestBuilder.post(requestBody)
            }
        }

        // Build the final request
        newRequest = requestBuilder.url(httpUrl)
            .headers(request.headers)
            .header(Session.API_KEY, encryptedApiKey!!)
            .header(Session.USER_SESSION, encryptedToken!!)
            .header(Session.LANGUAGE, language!!)
//            .header(Session.LANGUAGE, LocaleHelper.getLanguage(context).code)
            .build()

        // execute the request
        val proceed = chain.proceed(newRequest)

        // get the response body and decrypt it.
        val cipherBody = proceed.body!!.string()
        Log.d("fbfmkCIPHERbmf",cipherBody.toString())

        val plainBody = cryptLib.decrypt(cipherBody)

        val length = plainBody?.length ?:0
        val halfLenght = length/2
        Log.d("fblmfmkv", plainBody?.substring(0,halfLenght).toString())
        Log.d("Fbkfmvkf",plainBody?.substring(halfLenght,length).toString())

        Log.d("fbfmkbmf",plainBody.toString())

        val finalPlainBody = plainBody!!.trim { it <= ' ' }.toResponseBody("text/json".toMediaTypeOrNull())

        // create new Response with plaint text body for further process
        return proceed.newBuilder()
            .body(finalPlainBody)
            .build()
    }

    private fun bodyToString(request: RequestBody?): ByteArray {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ByteArray(0)
            return buffer.readByteArray()
        } catch (e: IOException) {
            return ByteArray(0)
        }
    }
}
*/
