package com.uae.core_network.models

data class ApiResponse(
    var status: Int?,
    val `data`: String,
    val message: String?,
    val count: Int? = null,
)





