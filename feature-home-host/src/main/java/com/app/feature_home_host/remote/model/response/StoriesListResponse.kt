package com.app.feature_home_host.remote.model.response


import com.google.gson.annotations.SerializedName

data class StoriesListResponse(
    val count: Int?,
    val `data`: List<StoryData?>?,
    val message: String?,
    val status: Int?
) {
    data class StoryData(
        val latestAt: String?,
        val stories: List<Story?>?,
        val user: User?
    ) {
        data class Story(
            val createdAt: String?,
            val expiresAt: String?,
            @SerializedName("_id")
            val id: String?,
            val mediaType: String?,
            val overlays: List<Any?>?,
            val url: String?
        )

        data class User(
            val chatId: String?,
            val fullName: String?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?,
            val rating: Int?
        )
    }
}