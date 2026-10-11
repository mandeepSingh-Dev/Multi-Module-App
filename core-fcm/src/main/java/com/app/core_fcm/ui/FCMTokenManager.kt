package com.app.core_fcm.ui

import android.util.Log
import androidx.compose.runtime.DisposableEffectScope
import com.google.firebase.messaging.FirebaseMessaging
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


class FCMTokenManager @Inject constructor(private val commonAllUseCases: CommonAllUseCases) {


    suspend fun registerFCMToken() = coroutineScope {
        val token = FirebaseMessaging.getInstance().token.addOnFailureListener {
            Log.d("fkbnkfbnf", it.toString())
        }.await()
        Log.d("cddsdsds", token.toString())
        commonAllUseCases.uploadFCMTokenResponse(fcmToken = token).onEach {
            it.getCallbacks(
                onLoading = {},
                onSuccess = {},
                onError = {
                    Log.d("FJNBJKFNB", it.error.toString())
                }
            )
        }.launchIn(this)
    }


    //    fun registerFCMToken() {
//        Log.d("fkbnkfbf", "registerFCMToken")
//        FirebaseMessaging.getInstance().register()
//    }
    fun deleteToken() {
        FirebaseMessaging.getInstance().deleteToken()
    }
//
}