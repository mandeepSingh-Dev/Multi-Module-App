package com.app.feature_leaderboard.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_leaderboard.domain.repository.LeaderboardRepository
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse.LeaderboardData.LeaderBoardItem
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class LeaderboardListDataSource(
    private val leaderboardRepository: LeaderboardRepository,
    private val onGetOtherData : (resetAt : String?, topThree: List<LeaderBoardItem?>?) -> Unit,
) : PagingSource<Int, LeaderboardListResponse.LeaderboardData.LeaderBoardItem>() {

    override fun getRefreshKey(state: PagingState<Int, LeaderboardListResponse.LeaderboardData.LeaderBoardItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LeaderboardListResponse.LeaderboardData.LeaderBoardItem> {
        val nextPage = params.key ?: 1


        val result = leaderboardRepository.getLeaderboardList(
            page = nextPage, limit = params.loadSize,
        ).first()



        return when (result) {
            is NetworkResult.Success -> {
                val data = result.data?.data
                val leaderBoardList = data?.list ?: emptyList()
                val isLastPage = leaderBoardList.isEmpty() ?: false

                onGetOtherData(data?.resetAt, data?.topThree)

                LoadResult.Page(
                    data = leaderBoardList,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (isLastPage) null else nextPage + 1
                )
            }

            is NetworkResult.Error -> {
                LoadResult.Error(Exception(result.error ?: "Unknown Error"))
            }

            else -> {
                LoadResult.Error(Exception("Unexpected result type"))
            }
        }
    }
}