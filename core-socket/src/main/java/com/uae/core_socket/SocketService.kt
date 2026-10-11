package com.uae.core_socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.app.core_common.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject


@AndroidEntryPoint
class SocketService : Service() {

    var socketIOManager : SocketIOManager? = null

    @Inject
    lateinit var userManager : UserManager

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        coroutineScope.launch {
//            val authToken = userManager.getUserToken()
//            socketIOManager?.connect(authToken = authToken)
//        }
        return super.onStartCommand(intent, flags, startId)
    }


}