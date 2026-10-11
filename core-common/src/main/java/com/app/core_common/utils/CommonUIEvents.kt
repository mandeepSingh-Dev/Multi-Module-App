package com.app.core_common.utils

import androidx.navigation3.runtime.NavKey
import com.app.core_common.base.UIEvent

sealed interface CommonUIEvents : UIEvent{

    data class ShowMessage(val message : String?) : CommonUIEvents
    data class NavigateTo(val navKey: NavKey) : CommonUIEvents

    data class ShowError(val error : String?) : CommonUIEvents

}