package com.app.core_navigation

import android.content.Context

interface CallNavigator {

    fun startCallActivity(context : Context,
                          chatID : String?,
                          callId : String? = null,
                          partnerID : String?,
                          personName : String?,
                          profilePic : String?,
                          callType : String?,
                          isFromInComingCall : Boolean = false
                      )

}

object NavigationProvider{
    lateinit var callNavigator : CallNavigator
}
