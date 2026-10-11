package com.app.feature_home_host.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.core_common.base.BaseViewModel
import com.app.feature_home_host.domain.HomeHosAllUseCases
import com.app.feature_home_host.domain.repository.HomeHostRepository
import com.app.feature_home_host.pagination.ProfileVisitorsListDataSource
import com.app.feature_home_host.remote.model.response.ProfileVisitorsListResponse
import com.app.feature_home_host.ui.enums.VisitorRangeType
import com.app.feature_home_host.ui.states.ProfileVisitorsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ProfileVisitorsViewModel @Inject constructor(
    private val homeHosAllUseCases: HomeHosAllUseCases,
    private val homeHostRepository: HomeHostRepository,
) : BaseViewModel<ProfileVisitorsUIState>(ProfileVisitorsUIState()) {

    private val _isFirstTimeApiHit = MutableStateFlow(false)
    val isFirstTimeApiHit = _isFirstTimeApiHit.asStateFlow()

    val allVisitorsListPaging = Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = {
            ProfileVisitorsListDataSource(
                range = VisitorRangeType.ALL.value,
                homeHostRepository = homeHostRepository,
            )
        }
    ).flow.cachedIn(viewModelScope)



    val sevenDaysVisitorsListPaging = Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = {
            ProfileVisitorsListDataSource(
                range = VisitorRangeType.SEVEN_DAYS.value,
                homeHostRepository = homeHostRepository,
            )
        }
    ).flow.cachedIn(viewModelScope)


    fun getProfileVisitors(range: String?): Flow<PagingData<ProfileVisitorsListResponse.Data.ProfileVisitorData>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                ProfileVisitorsListDataSource(
                    range = range,
                    homeHostRepository = homeHostRepository,
                )
            }
        ).flow
    }


}