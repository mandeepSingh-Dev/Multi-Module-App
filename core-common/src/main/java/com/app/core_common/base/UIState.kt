package com.app.core_common.base

interface UIState



open class CommonState(
    open val isLoading: Boolean = false
) : UIState