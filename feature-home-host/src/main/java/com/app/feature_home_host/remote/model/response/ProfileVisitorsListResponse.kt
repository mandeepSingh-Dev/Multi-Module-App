package com.app.feature_home_host.remote.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileVisitorsListResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class Data(
        val isSubscribed: Boolean?,
        val visitors: List<ProfileVisitorData>?
    ) {
        @Keep
        data class ProfileVisitorData(
            @SerializedName("_id")
            val id: String?,
            val isLocked: Boolean?,
            val lastViewedAt: String?,
            val viewer: Viewer?
        ) {
            @Keep
            data class Viewer(
                val age: Any?,
                val city: String?,
                val country: Any?,
                @SerializedName("_id")
                val id: String?,
                val isBlurred: Boolean?,
                val name: String?,
                val photo: Any?
            )
        }
    }
}