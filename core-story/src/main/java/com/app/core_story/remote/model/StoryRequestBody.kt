package com.app.core_story.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.util.UUID

class StoryRequestBody : ArrayList<StoryRequestBody.StoryRequestBodyItem>(){
    @Keep
    data class StoryRequestBodyItem(
        val mediaType: String?,
        val overlays: List<StoryRequestOverlay?>?,
        val url: String?
    )

    data class StoryRequestOverlay(
        @Transient val id: String = UUID.randomUUID().toString(),
        @SerializedName("type") val type: String?,       // "emoji" | "text"
        @SerializedName("x")
        val x: Double? = null,
        @SerializedName("y")
        val y: Double? = null,
        @SerializedName("scale")
        val scale: Double?= null,
        @SerializedName("rotation")
        val rotation: Double?= null,
        @SerializedName("value")
        val value: String? = null,
        @SerializedName("color")
        val color: String? = null,
        @SerializedName("fontSize")
        val fontSize: Double? = null,
        @SerializedName("backgroundColor")
        val backgroundColor: String? = null
    )
}