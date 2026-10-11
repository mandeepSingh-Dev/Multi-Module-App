package com.app.feature_leaderboard.remote.model.response


import androidx.annotation.Keep

@Keep
data class LeaderboardListResponse(
    val count: Int?,
    val `data`: LeaderboardData?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class LeaderboardData(
        val list: List<LeaderBoardItem>?,
        val resetAt: String?,
        val topThree: List<LeaderBoardItem?>?
    ) {
        @Keep
        data class LeaderBoardItem(
            val beans: Int?,
            val beansFormatted: String?,
            val fullName: String?,
            val profilePic: String?,
            val rank: Int?,
            val userId: String?
        )


    }
}