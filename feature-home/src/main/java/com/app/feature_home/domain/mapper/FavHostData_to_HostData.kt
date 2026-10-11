package com.app.feature_home.domain.mapper

import com.app.feature_home.remote.model.response.HostData
import com.uae.core_network.data.model.FavouriteHostsListResponse



fun FavouriteHostsListResponse.FavHostData.toHostData(): HostData {

    return HostData(
        address = this.address,
        about = this.about,
        age = this.age,
        chatId = this.chatId,
        city = this.city,
        sign = this.sign,
        emailVerified = this.emailVerified,
        fullName = this.fullName,
        gender = this.gender,
        id = this.id,
        isOnline = this.isOnline,
        isBlock = this.isBlock,
        isVerified = this.isVerified,
        isWishlisted = this.isWishlisted,
        lastSeen = this.lastSeen,
        phoneVerified = this.phoneVerified,
        profilePic = this.profilePic,
        languages = this.languages,
        level = this.level,
        totalTalkTime = this.totalTalkTime,
        rating = this.rating,
        role = this.role,
        isCallControllerViewOpen = this.isCallControllerViewOpen,
        isMenuOpen = this.isMenuOpen
    )

}