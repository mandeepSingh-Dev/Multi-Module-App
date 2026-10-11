package com.app.feature_home.remote.model.response

data class HostDetailsDataResponse(
    val count: Int?,
    val `data`: HostDetailsData?,
    val message: String?,
    val status: Int?
)

data class HostDetailsData(
    val user : HostData? = HostData(),
    val stories : List<Story?>? = null
)