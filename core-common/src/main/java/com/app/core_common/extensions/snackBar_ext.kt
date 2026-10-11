package com.app.core_common.extensions

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun SnackbarHostState.showSnackBarWithDismiss( message: String?, duration: SnackbarDuration = SnackbarDuration.Short) {
    message?.let {
        showSnackbar(message = message,
            withDismissAction = true,
            duration = duration,

            )
    }
}


suspend fun SnackbarHostState.showSnackBarWithAction(message: String?, duration: SnackbarDuration = SnackbarDuration.Short,
                                                     actionLabel : String? = null,
                                                     onAction : () -> Unit) {
    message?.let {
        val result  = showSnackbar(message = message, withDismissAction = false, duration = duration,
            actionLabel = actionLabel)
        if (result == SnackbarResult.ActionPerformed) {
            onAction()
        }
    }
}

