package com.uae.core_network.networkUtils

import android.util.Log
import okio.IOException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun handleUseCaseException(e: Throwable) : String{


    return when(e){
        is HttpException -> {
            networkCodeError(e)
        }
        is UnknownHostException -> {
            Log.d("fkvnvkfmnvf", "1")
            "Can’t reach the server right now. Please try again later."
        }
        is ConnectException, is SocketException, is SocketTimeoutException -> {
            Log.d("fkvnvkfmnvf", "2")
            "Connection Error: Unable to establish a connection to the server."
        }
        is SSLException -> "Secure connection failed. Please try again later."
        is IOException -> {
            "Connection Error: Unable to establish a connection to the server."
        }
        else -> {
            e.message ?: "Something went wrong. Please try again."
        }
    }
}

fun networkCodeError(exception : HttpException): String{

    return when(exception.code()){
        HttpURLConnection.HTTP_BAD_REQUEST ->  "Invalid request. Please Try again."
        HttpURLConnection.HTTP_UNAUTHORIZED -> "Unauthorized access. Login again!"
        HttpURLConnection.HTTP_PAYMENT_REQUIRED -> "Payment Required"
        HttpURLConnection.HTTP_FORBIDDEN -> "You do not have permission to access this resource."
        HttpURLConnection.HTTP_NOT_FOUND -> "The requested resource could not be found."
        HttpURLConnection.HTTP_INTERNAL_ERROR -> "Server Not Responding. Please try again later."
        HttpURLConnection.HTTP_BAD_GATEWAY -> "Server Not Responding. Please try again later."
        HttpURLConnection.HTTP_UNAVAILABLE -> "Service is currently unavailable. Please try again later."
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> "The server is taking too long to respond. Please try again later."

        else -> {exception.message.toString()}
    }
}