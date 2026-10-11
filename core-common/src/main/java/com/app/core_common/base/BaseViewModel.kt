package com.app.core_common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.app.core_common.UserManager
import com.app.core_common.utils.CommonUIEvents
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : UIState>(initialUiState: T? = null) : ViewModel(){


    private val _uiState = MutableStateFlow(initialUiState)
    val uiState = _uiState.asStateFlow()


    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()



    fun updateState(update : (T) -> T){
        _uiState.update {state ->
            state?.let {
                update(it)
            }
        }
    }

    fun onEvent(event : UIEvent) = viewModelScope.launch{
        _uiEvent.emit(event)
    }


    fun messageEvent(message : String?){
        onEvent(CommonUIEvents.ShowMessage(message))
    }
    fun navigationEvent(navKey : NavKey){
        onEvent(CommonUIEvents.NavigateTo(navKey))
    }
    fun errorEvent(error : String?){
        onEvent(CommonUIEvents.ShowError(error))
    }



}