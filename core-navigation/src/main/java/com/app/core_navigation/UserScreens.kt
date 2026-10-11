package com.app.core_navigation

import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.UserScreens.AuthScreens
import kotlinx.serialization.Serializable

@Serializable
sealed interface UserScreens : NavKey{


    @Serializable
    sealed interface AuthScreens : UserScreens {

        @Serializable
        data class LoginScreen(val dob : String?) : AuthScreens
        @Serializable
        data class ProfileScreen(val dob : String?, val step: Int?) : AuthScreens

        @Serializable
        object AgeConfirmationScreen : AuthScreens
        @Serializable
        object ProfileUpdateScreen : AuthScreens

    }



    @Serializable
    data class HostDetailsScreen(
        val id: String?,
        val chatId: String?,
        val fullName: String?,
        val profilePic: String?,
        val rating: Double?,
        val isVerified: Boolean?,
        val isWishlist: Boolean?,
        val gender: Int?
    ) : UserScreens

    @Serializable
    object FavouriteHostsScreen : UserScreens
    @Serializable
    object DaysStreakScreen : UserScreens

}
@Serializable
sealed interface WalletScreens : NavKey{
    @Serializable
    object MyCoinsScreen: WalletScreens
    @Serializable
    object CoinsRechargeScreen: WalletScreens
    @Serializable
    object TransactionsHistoryScreen : WalletScreens

}


@Serializable
sealed interface CallScreens : NavKey{
    @Serializable
    object CallHistoryScreen: CallScreens
}
@Serializable
sealed interface ReferralScreens : NavKey{
    @Serializable
    object ReferralScreen: CallScreens
    @Serializable
    object ApplyReferralCodeScreen: CallScreens
}

