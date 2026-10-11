package com.app.kahoji

import android.app.Application
import android.util.Log
import com.app.core_common.utils.SocketHostEventsHandler
import com.app.core_common.utils.deepLinkHandler
import com.app.core_common.utils.fromJson
import com.app.core_navigation.NavigationProvider
import com.app.feature_call.ui.CallNavigatorImpl
import com.app.feature_story.remote.model.response.StoriesListResponse
import com.uae.core_socket.SocketIOManager
import com.uae.core_socket.model.IncomingCallResponse
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltAndroidApp
class BaseApp : Application(){
    override fun onCreate() {
        super.onCreate()

        NavigationProvider.callNavigator = CallNavigatorImpl()

        listeningSocketIOEvents()
    }

    fun listeningSocketIOEvents(){
        CoroutineScope(Dispatchers.IO).launch {
            SocketIOManager.events.collectLatest {socketEvent ->
                Log.d("fkfnbjkfnb", socketEvent.toString())
                when(socketEvent){
                    is SocketIOManager.SocketEvent.IncomingCall -> {

                        val incomingCallData = socketEvent.data?.toString()?.fromJson<IncomingCallResponse>()

                        NavigationProvider.callNavigator.startCallActivity(
                            this@BaseApp,
                            chatID = incomingCallData?.chatId,
                            callId = incomingCallData?.callId,
                            partnerID = incomingCallData?.callerId,
                            callType = incomingCallData?.type,
                            personName = incomingCallData?.caller?.fullName,
                            profilePic = incomingCallData?.caller?.profilePic,
                            isFromInComingCall = true
                        )
                    }
                    is SocketIOManager.SocketEvent.GiftReceived -> {
                        SocketHostEventsHandler._receiveGift.emit(socketEvent.data)
                    }

                    else -> Unit
                }
            }
        }
    }

}