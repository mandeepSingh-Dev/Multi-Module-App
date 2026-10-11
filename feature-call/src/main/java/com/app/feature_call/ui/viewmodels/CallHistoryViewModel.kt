package com.app.feature_call.ui.viewmodels

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.enums.CallType
import com.app.feature_call.domain.repository.CallRepository
import com.app.feature_call.paginations.CallHistoryDataSource
import com.app.feature_call.remote.model.response.CallHistoryResponse
import com.app.feature_call.ui.states.CallHistoryUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CallHistoryViewModel @Inject constructor(
    private val callRepository: CallRepository
) : BaseViewModel<CallHistoryUIState>(CallHistoryUIState()) {
    private val _isFirstTimeApiHit = MutableStateFlow(false)
    val isFirstTimeApiHit = _isFirstTimeApiHit.asStateFlow()


    val allCallHistory = getCallHistory(null)
    val videoCallHistory = getCallHistory(CallType.Video.type)
    val audioCallHistory = getCallHistory(CallType.Audio.type)


    fun getCallHistory(type : String?): Flow<PagingData<CallHistoryResponse.CallHistoryData>> {
        _isFirstTimeApiHit.value = true
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                CallHistoryDataSource(
                    type = type,
                    callRepository = callRepository
                )
            }
        ).flow
    }
}