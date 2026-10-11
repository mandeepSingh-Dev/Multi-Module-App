package com.app.feature_home_host.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.core_ui.enums.GenderType
import com.app.feature_home_host.domain.repository.HomeHostRepository
import com.app.feature_home_host.remote.model.response.ProfileVisitorsListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class ProfileVisitorsListDataSource(
    private val range: String? = null,
    private val homeHostRepository: HomeHostRepository,
) : PagingSource<Int, ProfileVisitorsListResponse.Data.ProfileVisitorData>() {

    override fun getRefreshKey(state: PagingState<Int, ProfileVisitorsListResponse.Data.ProfileVisitorData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProfileVisitorsListResponse.Data.ProfileVisitorData> {
        val nextPage = params.key ?: 1


        val result = homeHostRepository.getProfileVisitors(
            page = nextPage, limit = params.loadSize,
            range = range
        ).first()



        return when (result) {
            is NetworkResult.Success -> {
                val visitorsList = result.data?.data?.visitors ?: emptyList()
                val isLastPage = visitorsList.isEmpty() ?: false

                result.data?.data

                LoadResult.Page(
                    data = visitorsList,
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