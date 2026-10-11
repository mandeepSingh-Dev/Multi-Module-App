package com.app.feature_chat.navigation.authGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.ChatScreens
import com.app.feature_chat.ui.screens.ChatScreen


fun EntryProviderScope<NavKey>.chatGraph() {

    entry<ChatScreens.ChatScreen> {
        ChatScreen(
            userType = it.userType,
            partnerName = it.partnerName,
            partnerProfilePic = it.partnerProfilePic,
            partnerID = it.partnerID,
            isPartnerFav = it.isPartnerFav,
            isOnline = it.isOnline,
            chatID = it.chatID,
            isVerified = it.isVerified,
            gender = it.gender
        )
    }
}
