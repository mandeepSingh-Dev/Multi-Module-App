package com.uae.core_network.networkUtils

sealed class NetworkResult<T> (data:T? = null, message: String? = null, error: String? = null, errorCode : Int? = null, bodyData: Any? = null) {

    class Loading<T> : NetworkResult<T>(null,null, null , null,null)
    class Success<T>(val data: T?,val message:String? = null) : NetworkResult<T>(data = data, message = message, error = null, errorCode = null, bodyData = null)
    class Error<T>(val error : String?, val errorCode : Int? = null, var bodyData: Any? = null) : NetworkResult<T>(data =null,message = null, error = error, errorCode = errorCode,bodyData = null)
//    class Nothing<T>() : NetworkResult<T>()
}

 fun <T> NetworkResult<T>.getCallbacks(onLoading: () -> Unit,
                                              onSuccess : (NetworkResult.Success<T>) -> Unit,
                                              onError: (NetworkResult.Error<T>) -> Unit) {
    when(this){
        is NetworkResult.Loading -> {
            onLoading()
        }
        is NetworkResult.Success -> {
            onSuccess(this)
        }
        is NetworkResult.Error -> {
            onError(this)
        }
    }
}
