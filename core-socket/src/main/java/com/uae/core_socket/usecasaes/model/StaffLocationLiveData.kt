package com.uae.core_socket.usecasaes.model


import com.google.gson.annotations.SerializedName

data class StaffLocationLiveData(
    val latitude: Double?,
    val longitude: Double?,
    val staffId: String?,
    val updatedAt: String?
)