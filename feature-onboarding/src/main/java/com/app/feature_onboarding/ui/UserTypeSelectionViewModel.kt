package com.app.feature_onboarding.ui

import androidx.lifecycle.viewModelScope
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.core_fcm.ui.FCMTokenManager
import com.uae.core_socket.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class UserTypeSelectionViewModel @Inject constructor(private val userMger : UserManager,
    private val fcmTokenManager: FCMTokenManager) : BaseViewModel<UIState>(){

        init {
            viewModelScope.launch {
                clearAllData(userMger)
            }
        }

    suspend fun clearAllData(userMger: UserManager){
        userMger.clearLocalData()
        fcmTokenManager.deleteToken()
        SocketIOManager.clear()
    }

}