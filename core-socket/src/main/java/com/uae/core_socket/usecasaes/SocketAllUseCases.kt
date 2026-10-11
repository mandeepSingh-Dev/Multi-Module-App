package com.uae.core_socket.usecasaes

import android.content.Context
import android.util.Log
import com.uae.core_socket.SocketConstants
import com.uae.core_socket.SocketIOManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject


data class SocketAllUseCases @Inject constructor(
    val socketLocationUpdateUseCase : SocketLocationUpdateUseCase
)

class SocketLocationUpdateUseCase @Inject constructor(@ApplicationContext val context: Context){

     operator fun invoke(latitude : Double?, longitude : Double?) {
         val obj = JSONObject()
        obj.put(SocketConstants.LATITUDE, latitude)
        obj.put(SocketConstants.LONGITUDE, longitude)
        SocketIOManager.emit(event = SocketIOManager.UPDATE_CUSTOMER_LOCATION, obj)
    }
}