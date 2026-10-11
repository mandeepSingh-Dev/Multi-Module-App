package com.app.core_fcm.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.core_common.utils.fromJson
import com.app.core_common.utils.toJson
import com.app.core_fcm.R
import com.app.core_fcm.model.PushNotificationData
import com.app.core_ui.ui.theme.theme_color_1
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject


@AndroidEntryPoint
class FCMService  : FirebaseMessagingService() {

    companion object{
        const val TAG = "FirebaseService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"Created")
    }
    @Inject
    lateinit var commonAllUseCases: CommonAllUseCases

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
Log.d(TAG, "onRegistered: $installationId")
//        commonAllUseCases.uploadFCMTokenResponse(fcmToken = installationId).launchIn(coroutineScope)

    }

    override fun onUnregistered(installationId: String) {
        super.onUnregistered(installationId)
//        Log.d(TAG, "onUnRegistered: $installationId")
    }

    @Deprecated("Deprecated in Java")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            commonAllUseCases.uploadFCMTokenResponse(fcmToken = it).launchIn(coroutineScope)
        }
    }


    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)

        intent?.extras?.keySet()?.forEach {
            Log.d("Dvldvkndkvnd", "$it -> ${intent.extras?.get(it)}")
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("fkbnfkbnf", message.notification?.title.toString())
        Log.d("fkbnfkbnf", "Date ->" + message.data.toString())

        message.data.let {
            it.put("title", message.notification?.title)
            it.put("body", message.notification?.body)
            val json = it.toJson()
            val notificationData = json?.fromJson<PushNotificationData>()
            notificationData?.let {
                showNotification(it)
            }
        }
    }

    private fun showNotification(data: PushNotificationData) {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), importance).apply {
            enableLights(true)
            enableVibration(true)
        }
        manager.createNotificationChannel(channel)

//        val orderDetailsActivity = Intent(this, OrderDetailsActivity::class.java)
//        orderDetailsActivity.putExtra(Constants.IntentKeys.ORDER_ID, pushNotificationData.action_id)
//        orderDetailsActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK

//        val pendingIntent = PendingIntent.getActivity(this,0, orderDetailsActivity, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))

            .setContentTitle(data.title)
            .setSmallIcon(com.app.core_ui.R.drawable.logo_1image1)
            .setContentText(data.description)
            .setOngoing(false)
            .setAutoCancel(true)
            .setColor(getColor(com.app.core_ui.R.color.theme_color_1))
//            .setContentIntent(pendingIntent)
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(pushNotificationData.body)
//            )

            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }



}


