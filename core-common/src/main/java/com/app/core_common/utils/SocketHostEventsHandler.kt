package com.app.core_common.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

object SocketHostEventsHandler {


    val _receiveGift = MutableSharedFlow<Any?>()
    val receiveGift = _receiveGift.asSharedFlow()

}