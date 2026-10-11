package com.app.feature_home_host.remote.model.response


import com.google.gson.annotations.SerializedName

data class HomeDataUserResponse(
    val count: Int?,
    val `data`: HomeDataUser?,
    val message: String?,
    val status: Int?
) {
    data class HomeDataUser(
        val onlineNow: List<OnlineNow?>?,
        val recomended: List<Recomended?>?
    ) {
        data class OnlineNow(
            val address: String?,
            val age: Int?,
            val chatId: String?,
            val city: Any?,
            val emailVerified: Int?,
            val fullName: String?,
            val gender: Int?,
            @SerializedName("_id")
            val id: String?,
            val isOnline: Int?,
            val isVerified: Boolean?,
            val isWishlisted: Boolean?,
            val lastSeen: Any?,
            val phoneVerified: Int?,
            val profilePic: String?,
            val rating: Number?,
            val isCallControllerViewOpen : Boolean = false,
            val isMenuOpen : Boolean = false
        )

        data class Recomended(
            val address: String?,
            val age: Int?,
            val chatId: String?,
            val city: Any?,
            val emailVerified: Int?,
            val fullName: String?,
            val gender: Int?,
            @SerializedName("_id")
            val id: String?,
            val isOnline: Int?,
            val isVerified: Boolean?,
            val isWishlisted: Boolean?,
            val lastSeen: Any?,
            val matchCount: Int?,
            val phoneVerified: Int?,
            val profilePic: String?,
            val rating: Int?,
            val isCallControllerViewOpen : Boolean = false

        )
    }
}