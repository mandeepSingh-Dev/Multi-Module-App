package com.app.feature_home.remote.model.response

import com.google.gson.annotations.SerializedName

data class HostData(
        val address: String? = null,
        val about: String? = null,
        val age: Int?= null,
        val chatId: String?= null,
        val city: String?= null,
        val sign: String?= null,
        val emailVerified: Int?= null,
        val fullName: String?= null,
        val gender: Int?= null,
        @SerializedName("_id")
        val id: String?= null,
        val isOnline: Int?= null,
        val isBlock: Boolean?= null,
        val isVerified: Boolean?= null,
        val isWishlisted: Boolean?= null,
        val lastSeen: Any?= null,
        val phoneVerified: Int?= null,
        val profilePic: String?= null,
        val languages : List<String>?= null,
        val level : String?= null,
        val totalTalkTime : String? = null,
        val rating: Double?= null,
        val role: Int?= null,
        val isCallControllerViewOpen : Boolean = false,
        val isMenuOpen : Boolean = false
    ){
}

