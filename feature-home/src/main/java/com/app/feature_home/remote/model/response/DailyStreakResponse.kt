package com.app.feature_home.remote.model.response


import androidx.annotation.Keep

@Keep
data class DailyStreakResponse(
    val count: Int?,
    val `data`: DailyStreakData?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class DailyStreakData(
        val baseNextReward: Int?,
        val claimedToday: Boolean?,
        val currentStreak: Int?,
        val days: List<Day?>?,
        val isVip: Boolean?,
        val lastClaimDate: String?,
        val longestStreak: Int?,
        val nextClaimDay: Int?,
        val nextReward: Int?,
        val serverDate: String?,
        val vipMultiplier: Int?
    ) {
        @Keep
        data class Day(
            val baseReward: Int?,
            val day: Int?,
            val isCompleted: Boolean?,
            val isCurrent: Boolean?,
            val isLocked: Boolean?,
            val reward: Int?
        )
    }
}