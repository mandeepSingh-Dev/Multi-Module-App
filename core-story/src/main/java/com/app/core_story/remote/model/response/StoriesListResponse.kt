package com.app.core_story.remote.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class StoriesListResponse(
    val count: Int?,
    @SerializedName("data")
    val usersList: List<UserStoryGroup?>?,
    val message: String?,
    val status: Int?
) {
    data class UserStoryGroup(
        val latestAt: String?,
        val stories: List<Story?>?,
        val user: User?
    ) {
        data class Story(
            val index: Int,  // not from api
            val createdAt: String?,
            val expiresAt: String?,
            @SerializedName("_id")
            val id: String?,
            val mediaType: String?,
            val overlays: List<StoryOverlay?>? = listOf(
                StoryOverlay()
            ),
            val url: String?
        )

        data class User(
            val chatId: String?,
            val fullName: String?,
            val gender: Int?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?,
            val isVerified: Boolean?,
            val rating: Double?
        )

        data class StoryOverlay(
            val type: String? = null,
            val x: Double? = null,
            val y: Double? = null,
            val scale: Double? = null,
            val rotation: Double? = null,
            val value: String? = null,
            val color: String? = null,   //ask aditya to give this value
            val backgroundColor: String? = null,   //ask aditya to give this value
            val fontSize: Double? = null,   //ask aditya to give this value
        )
    }
}
