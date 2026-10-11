package com.app.core_common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.concurrent.timer
import kotlin.time.Duration.Companion.seconds

class Timer {

    var timerJob : Job? = null
    fun startTimer(scope: CoroutineScope,
                   onTimer : (String?) -> Unit){
        timerJob?.cancel()
        timerJob = scope.launch {
            var count = 0L
            while(isActive){
                delay(1.seconds)
                onTimer(formatDuration(count))
                count++
            }
        }
    }

    fun cancelTimer(){
        timerJob?.cancel()
    }

    fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours,
            minutes,
            remainingSeconds
        )
    }

}