package com.app.kahoji.ui

import com.app.core_common.base.BaseViewModel
import com.app.kahoji.MainActivityUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): BaseViewModel<MainActivityUIState>(MainActivityUIState()) {


    init {

    }

}