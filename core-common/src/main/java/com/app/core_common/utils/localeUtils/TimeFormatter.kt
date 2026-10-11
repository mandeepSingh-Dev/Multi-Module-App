package com.app.core_common.utils.localeUtils

import android.util.Log
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.util.concurrent.TimeUnit


fun Long.convertSecondsToHHMMSS(): String? {


    return    try {
        val hours = this / 3600
        val minutes = (this % 3600) / 60
        val secs = this % 60

        Log.d("kldmvkdmvd", hours.toString())

        val result = if(hours > 0){
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        }else {
            String.format("%02d:%02d", minutes, secs)
        }
        Log.d("kldmvkdmvd", result.toString())
        result
    }catch (e: Exception){
         null
    }
}

fun LocalDate?.getParsedDate(): String? {
    if(this == null) return null
    return "$year-$monthValue-$dayOfMonth"
}

fun Long.convertMillisecondsToHHMMSS() : String? {
    return try {
        val seconds = (this / 1000)
        Log.d("knvkdnvjkd", "$this -> $seconds")
         seconds.convertSecondsToHHMMSS()
    }catch (e: Exception){
        null
    }
}

fun getTimeLeft(targetIsoTime: String): String {
    // Parse ISO time
    val targetInstant = Instant.parse(targetIsoTime)

    // Current time (UTC)
    val nowInstant = Instant.now()

    // If current time is already past
    if (nowInstant >= targetInstant) {
        return "00:00:00 left"
    }

    // Duration difference
    val duration = Duration.between(nowInstant, targetInstant)

    val totalSeconds = duration.seconds

    val hours = TimeUnit.SECONDS.toHours(totalSeconds)
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
    val seconds = totalSeconds % 60

    // Format HH:mm:ss
    return String.format("%02d:%02d:%02d left", hours, minutes, seconds)
}