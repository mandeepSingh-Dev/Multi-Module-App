package com.app.feature_home_host.remote


data class DashboardStatsApiResponse(
    val count: Int?,
    val `data`: DashboardStatsData?,
    val message: String?,
    val status: Int?
) {
    data class DashboardStatsData(
        val calls: Calls?,
        val profileViews: ProfileViews?,
        val rating: Rating?,
        val talkTime: TalkTime?,
        val todayEarning: Number?,
    ) {
        data class Calls(
            val total: Int?
        )

        data class ProfileViews(
            val total: Int?
        )

        data class Rating(
            val average: Number?,
            val totalRatings: Number?
        )

        data class TalkTime(
            val formatted: String?,
            val seconds: Int?
        )
    }
}