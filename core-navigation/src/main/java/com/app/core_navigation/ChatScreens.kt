package com.app.core_navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface ChatScreens : NavKey {


    @Serializable
    data class ChatScreen(
        val userType : Int,
        val partnerName : String?,
        val partnerProfilePic : String?,
        val partnerID : String?,
        val isPartnerFav : Int?,
        val isOnline : Int?,
        val isVerified : Boolean?,
        val gender : Int?,
        val chatID : String?) : ChatScreens {
    }
}
