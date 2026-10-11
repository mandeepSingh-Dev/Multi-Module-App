package com.app.feature_withdraw.ui.states

import com.app.core_common.base.UIState
import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse.BeanPackData
import com.app.feature_withdraw.remote.model.response.WithdrawalSummaryDetailsResponse.WithdrawSummaryData

data class WithdrawBeanUIState(
    val isLoading : Boolean = false,
    val isWithdrawLoading : Boolean = false,
    val withdrawSummaryData : WithdrawSummaryData? = null,
    val isBeansPackListLoading : Boolean = false,
    val beanPackData: List<BeanPackData?>? = null,
    val selectedAmount : Int? = null

) : UIState