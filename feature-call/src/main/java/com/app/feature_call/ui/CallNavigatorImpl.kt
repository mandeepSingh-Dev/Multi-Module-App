package com.app.feature_call.ui

import android.content.Context
import android.content.Intent
import android.util.AndroidRuntimeException
import com.app.core_common.utils.Constants
import com.app.core_navigation.CallNavigator

class CallNavigatorImpl : CallNavigator {
    override fun startCallActivity(
        context: Context,
        chatID: String?,
        callID : String?,
        partnerID: String?,
        personName: String?,
        profilePic: String?,
        callType : String?,
        isFromInComingCall : Boolean
    ) {
        try {
            val intent = Intent(context, VideoActivity::class.java)
                .apply {
                    putExtra(Constants.IntentKeys.CHAT_ID, chatID)
                    putExtra(Constants.IntentKeys.PARTNER_ID, partnerID)
                    putExtra(Constants.IntentKeys.PERSON_NAME, personName)
                    putExtra(Constants.IntentKeys.CALL_TYPE, callType)
                    putExtra(Constants.IntentKeys.CALL_ID, callID)
                    putExtra(Constants.IntentKeys.PERSON_NAME, personName)
                    putExtra(Constants.IntentKeys.PERSON_IMAGE, profilePic)
                    putExtra(Constants.IntentKeys.IS_FROM_INCOMING_CALL, isFromInComingCall)
                }
            context.startActivity(intent)
        } catch (e: AndroidRuntimeException) {
            val intent = Intent(context, VideoActivity::class.java)
                .apply {
                    putExtra(Constants.IntentKeys.CHAT_ID, chatID)
                    putExtra(Constants.IntentKeys.PARTNER_ID, partnerID)
                    putExtra(Constants.IntentKeys.CALL_TYPE, callType)
                    putExtra(Constants.IntentKeys.CALL_ID, callID)
                    putExtra(Constants.IntentKeys.PERSON_NAME, personName)
                    putExtra(Constants.IntentKeys.PERSON_IMAGE, profilePic)
                    putExtra(Constants.IntentKeys.IS_FROM_INCOMING_CALL, isFromInComingCall)
                }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)

        }
    }

}