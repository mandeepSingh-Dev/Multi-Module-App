package com.app.core_common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner


@Composable
fun AttachLifecycleObserver(onEvent : (Lifecycle.Event) -> Unit) {


    val lifecycleOwner = rememberLifecycleOwner()
    DisposableEffect(lifecycleOwner) {

        val lifecycleObserver = LifecycleEventObserver { source, event ->
            onEvent(event)
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }


}