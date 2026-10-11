package com.app.core_common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.app.core_common.base.UIEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ObserveUIEvents(uiEvent: SharedFlow<UIEvent?>, onEvent : suspend (UIEvent?) -> Unit) {

    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest {
            onEvent(it)
        }
    }

}