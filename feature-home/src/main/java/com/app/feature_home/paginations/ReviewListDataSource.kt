package com.app.feature_home.paginations

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_home.domain.mapper.toHostData
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.model.response.HostData
import com.uae.core_network.domain.repository.CommonRepository
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class FavouriteHostsDataSource(
    private val commonRepository: CommonRepository,
) : PagingSource<Int, HostData>() {

    override fun getRefreshKey(state: PagingState<Int, HostData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HostData> {
        val nextPage = params.key ?: 1


        val result = commonRepository.getFavouriteHost(
            page = nextPage, limit = params.loadSize,
        ).first()



        return when (result) {
            is NetworkResult.Success -> {
                val reviewsList = (result.data?.data ?: emptyList()).map { it.toHostData() }

                val isLastPage = reviewsList.isEmpty() ?: false

                LoadResult.Page(
                    data = reviewsList,
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