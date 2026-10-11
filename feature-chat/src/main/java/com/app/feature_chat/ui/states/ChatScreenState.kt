package com.app.feature_chat.ui.states

import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState
import com.app.feature_chat.remote.model.response.ChatMessagesResponse.MessageChat

data class ChatScreenState (
    val isLoading: Boolean = false,
    val topLoading: Boolean = false,
    val isTextMessageSending: Boolean = false,
    val messages: List<MessageChat?>? = null,
    val futureListEmpty: Boolean = false,
    val message: String? = null,
    val profileData: ProfileResponse.UserProfileData? = null,
    val isPartnerFav: Int? = null,
    val isBlockLoading: Boolean = false,
    val isBlocked : Boolean = false
) : UIState