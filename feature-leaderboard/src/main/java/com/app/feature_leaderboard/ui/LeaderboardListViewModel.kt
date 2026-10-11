package com.app.feature_leaderboard.ui

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_common.utils.localeUtils.getTimeRemaining
import com.app.feature_leaderboard.domain.repository.LeaderboardRepository
import com.app.feature_leaderboard.pagination.LeaderboardListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LeaderboardListViewModel @Inject constructor(private val leaderboardRepository: LeaderboardRepository): BaseViewModel<LeaderboardListState>(
    LeaderboardListState()
) {

    val leaderboardListPaging =  Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = {
            LeaderboardListDataSource(
                leaderboardRepository = leaderboardRepository,
                onGetOtherData = {resetAt, topThree ->
                    updateState { state -> state.copy(resetAt = resetAt?.getTimeRemaining(), topThree = topThree) }
                }
            )
        }
    ).flow

}