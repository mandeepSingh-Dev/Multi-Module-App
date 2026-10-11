package com.app.feature_call.paginations

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_call.domain.repository.CallRepository
import com.app.feature_call.remote.model.response.CallHistoryResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class CallHistoryDataSource(
    private val type : String? = null,
    private val callRepository : CallRepository,
) : PagingSource<Int, CallHistoryResponse.CallHistoryData>() {

    override fun getRefreshKey(state: PagingState<Int, CallHistoryResponse.CallHistoryData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CallHistoryResponse.CallHistoryData> {
        val nextPage = params.key ?: 1

        val result = callRepository.getCallHistory(
            page = nextPage, limit = params.loadSize,
            type = type,
        ).first()



        return when(result){
            is NetworkResult.Success -> {
                val callHistoryList = result.data?.data ?: emptyList()
                val isLastPage = callHistoryList.isEmpty() ?: false

                LoadResult.Page(
                    data = callHistoryList,
                    prevKey = if(nextPage == 1) null else nextPage - 1,
                    nextKey = if(isLastPage) null else nextPage + 1
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