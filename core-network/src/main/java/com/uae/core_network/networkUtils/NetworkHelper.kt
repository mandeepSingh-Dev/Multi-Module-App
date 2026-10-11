package com.uae.core_network.networkUtils

import com.uae.core_network.models.ApiResponse2
import android.content.Context
import android.util.Log
import com.app.core_common.utils.fromJson
import com.google.gson.GsonBuilder
import com.uae.core_network.models.ErrorResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class NetworkHelper @Inject constructor(
    @ApplicationContext val context: Context,
) {
     inline fun <reified T> executeWithResponseManipulate(
        crossinline call: suspend () -> Response<T>,
        crossinline onSuccess: suspend (response: Response<T>) -> Response<T>,
        crossinline onError: suspend (exception: String) -> Unit,
        shouldLoading: Boolean = true
    ): Flow<NetworkResult<T>> = flow {

        if(shouldLoading){
            emit(NetworkResult.Loading())
        }
        val response = call()

        val result = if (response.isSuccessful) {
            val updatedResponse = onSuccess(response)
            NetworkResult.Success(updatedResponse.body())
        } else {
            val gson = GsonBuilder().create()
            val errorBody = gson.fromJson(response.errorBody()?.string(), ApiResponse2::class.java)
            if (response.code() == 400 && errorBody.status == 0) {
                NetworkResult.Error(handleUseCaseException(Exception(errorBody.message)))
            } else if (response.code() == 403 && errorBody.status == 0) {
                NetworkResult.Error(handleUseCaseException(Exception(errorBody.message)))
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

//                clearAllUserData()

                val httpException = HttpException(response)
                NetworkResult.Error(handleUseCaseException(httpException))
            } else {
                val networkException = HttpException(response)
                NetworkResult.Error(handleUseCaseException(networkException))
            }
        }
        emit(result)
    }.retryWhen { throwable, count ->
        return@retryWhen if (throwable is IOException && count < 3) {
            delay(1000 * (count + 1))
            true
        } else {
            false
        }
    }.catch { t ->
        emit(NetworkResult.Error(handleUseCaseException(t)))
    }.flowOn(Dispatchers.IO)


    inline fun <reified T> executeWithFlow(
        crossinline call: suspend () -> Response<T>,
        crossinline onSuccess: suspend (response: Response<T>) -> String?,
        crossinline onError: suspend (exception: String) -> Unit
    ): Flow<NetworkResult<T>> = flow {

        emit(NetworkResult.Loading())

        val response = call()

        val result = if (response.isSuccessful) {
            val exception = onSuccess(response)
            if (exception == null) {
                NetworkResult.Success(response.body())
            } else {
                NetworkResult.Error(exception)
            }
        } else {
            val errorBody = response.errorBody()?.string()?.fromJson<ErrorResponse>()
            Log.d("flbmkfmvf", errorBody.toString())
            Log.d("flbmkfmvf", response.code().toString() + " ResponseCode")

            if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST && errorBody?.status == 0) {
                NetworkResult.Error(handleUseCaseException(Exception(errorBody.message)))
            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN && errorBody?.status == 0) {
                NetworkResult.Error(handleUseCaseException(Exception(errorBody.message)))
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                clearAllUserData()
                val httpException = HttpException(response)
                NetworkResult.Error(
                    error = handleUseCaseException(httpException),
                    errorCode = response.code()
                )
            } else {
                Log.d("fkbmkfmv", response.code().toString())
                NetworkResult.Error(
                    error = handleUseCaseException(Exception(errorBody?.message)),
                    errorCode = response.code()
                )
            }
        }
        emit(result)
    }
        .catch { e ->
        emit(NetworkResult.Error(handleUseCaseException(e)))
    }.flowOn(Dispatchers.IO)

    inline fun <reified T> executeWithRetryFlow(
        crossinline call: suspend () -> Response<T>,
        crossinline onSuccess: suspend (response: Response<T>) -> String?,
        crossinline onError: suspend (exception: String) -> Unit,
        shouldLoading : Boolean = true
    ): Flow<NetworkResult<T>> = flow {

        if(shouldLoading) {
            emit(NetworkResult.Loading())
        }
        val response = call()

        val result = if (response.isSuccessful) {
            val exception = onSuccess(response)

            if (exception == null) {
                NetworkResult.Success(response.body())
            } else {
                NetworkResult.Error(exception)
            }
        } else {
            Log.d("flvmfkvmfkbf", "ERROR")
            val gson = GsonBuilder().create()
            val errorBody = gson.fromJson(response.errorBody()?.string(), ApiResponse2::class.java)
            Log.d("flbmkfmvf", errorBody.message.toString())
            Log.d("flbmkfmvf", response.code().toString() + " ResponseCode")

            if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST && errorBody.status == 0) {
                NetworkResult.Error(handleUseCaseException(Exception(errorBody.message)))
            } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN && errorBody.status == 0) {
                NetworkResult.Error(handleUseCaseException(Exception(errorBody.message)))
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                clearAllUserData()
                val httpException = HttpException(response)
                NetworkResult.Error(
                    error = handleUseCaseException(httpException),
                    errorCode = response.code()
                )
            } else {
                NetworkResult.Error(
                    error = handleUseCaseException(Exception(errorBody.message)),
                    errorCode = response.code()
                )
            }
        }
        emit(result)
    }.retryWhen { throwable, count ->
        return@retryWhen if (throwable is IOException && count < 3) {
            delay(1000 * (count + 1))
            true
        } else {
            false
        }
    }.catch { t ->
        emit(NetworkResult.Error(handleUseCaseException(t)))
    }.flowOn(Dispatchers.IO)
}
