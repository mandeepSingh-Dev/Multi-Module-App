package com.app.feature_splash.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.Constants
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.AppScreens
import com.app.core_navigation.HostScreens
import com.app.core_navigation.UserScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userManager: UserManager
): BaseViewModel<UIState>(){


    var splashJob : Job? = null
    fun startTimer(){
        splashJob?.cancel()
        splashJob = viewModelScope.launch(Dispatchers.IO) {
            delay(Constants.SPLASH_DELAY.milliseconds)

            checkNavigation()
        }
    }


    suspend fun checkNavigation(){
        if(userManager.isLoggedIn()){
            Log.d("fkbnkfbn", userManager.getUserType().toString())
            if(userManager.getUserType() == UserType.USER.value){
                onEvent(CommonUIEvents.NavigateTo(AppScreens.HomeBottomContainerScreen))
            }else{
                onEvent(CommonUIEvents.NavigateTo(AppScreens.HomeBottomContainerHostScreen))
            }
        }else{
            onEvent(CommonUIEvents.NavigateTo(AppScreens.OnBoardingScreen))
        }
    }

}