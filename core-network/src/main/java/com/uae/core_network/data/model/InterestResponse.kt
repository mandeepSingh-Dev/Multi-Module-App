package com.uae.core_network.data.model

import com.google.gson.annotations.SerializedName

data class InterestResponse(
    val status: Int?,
    val message: String?,
    val data: List<Interest>?,
    val count: Int?
)

data class Interest(
    val _id: String?,
    val name: String?,
    val imgSrc: String?,
    val active: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?,
    @Transient
    val isSelected : Boolean = false
)
