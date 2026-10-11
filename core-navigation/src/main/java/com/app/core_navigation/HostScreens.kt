package com.app.core_navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface HostScreens : NavKey {

    @Serializable
    sealed interface AuthHostScreens : NavKey {

        @Serializable
        object LoginHostScreen : AuthHostScreens

        @Serializable
        data class SignUpHostScreen(
            val phone: String?,
            val countryCode: String?,
            val step: Int?,
            val isForUpdate: Boolean = false,
        ) : AuthHostScreens
    }

    @Serializable
    object HostProfileVisitorsScreen : AppScreens


    @Serializable
    data class VisitorDetailsScreen(val visitorId: String?) : HostScreens

}

@Serializable
object WithDrawScreens : NavKey {
    @Serializable
    object WithdrawBeansScreens : HostScreens {}

    @Serializable
    object IdentityVerificationScreen : HostScreens {}

    @Serializable
    object FaceVerificationScreen : HostScreens {}

}


@Serializable
object BlockScreens : NavKey {
    @Serializable
    data class BlockScreen(val userType: Int) : HostScreens {}
}