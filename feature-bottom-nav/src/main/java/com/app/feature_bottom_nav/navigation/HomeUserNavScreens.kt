package com.app.feature_bottom_nav.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeUserNavScreens : NavKey {

    @Serializable
    object HomeScreen : HomeUserNavScreens
    @Serializable
    data class HomeSearchScreen(val showOnlineProfiles : Boolean = false) : HomeUserNavScreens
    @Serializable
    object ChatScreen : HomeUserNavScreens
    @Serializable
    object ProfileScreen : HomeUserNavScreens

}
@Serializable
sealed interface HomeHostNavScreens : NavKey {

    @Serializable
    object HomeScreen : HomeUserNavScreens
    @Serializable
    object EarningsScreen : HomeUserNavScreens
    @Serializable
    object ChatScreen : HomeUserNavScreens
    @Serializable
    object ProfileScreen : HomeUserNavScreens

}


enum class HomeUserNavScreensTypes(val value : Int){
    HOME_SCREEN(0),
    SEARCH_SCREEN(1),
    CHAT_LIST_SCREEN(2),
    PROFILE_SETTINGS_SCREEN(3),
}
enum class HomeHostNavScreensTypes(val value : Int){
    HOME_SCREEN(0),
    EARNING_SCREEN(1),
    CHAT_LIST_SCREEN(2),
    PROFILE_SETTINGS_SCREEN(3),
}