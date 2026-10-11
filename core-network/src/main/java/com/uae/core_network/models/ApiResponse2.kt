package com.uae.core_network.models

data class ApiResponse2(
    var status: Int?,
    val `data`: Data?,
    val message: String?,
    val count: Int? = null,
)
data class ApiResponse3(
    var status: Int?,
    val `data`: Any?,
    val message: String?,
    val count: Int? = null,
)
data class Data(var _id : String? = null)

data class ErrorResponse(
    var status: Int?,
    val `data`: Any?,
    val message: String?,
    val count: Int? = null,
)
