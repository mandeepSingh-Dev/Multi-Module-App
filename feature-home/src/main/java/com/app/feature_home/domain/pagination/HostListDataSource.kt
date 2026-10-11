package com.app.feature_home.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.core_ui.enums.GenderType
import com.app.feature_home.domain.model.HostFilterState
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.model.response.ChatListResponse
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.remote.model.response.HostListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class HostListDataSource(
    private val search : String? = null,
    private val filterState : HostFilterState? = null,
    private val homeUserRepository : HomeUserRepository,
) : PagingSource<Int,HostData>() {

    override fun getRefreshKey(state: PagingState<Int, HostData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HostData> {
        val nextPage = params.key ?: 1

        val gender = when(filterState?.gender){
            GenderType.MALE.type -> "male"
            GenderType.FEMALE.type -> "male"
            GenderType.PREFER_NOT_SAY.type -> "all"
            else -> null
        }

        val result = homeUserRepository.getHostList(
            page = nextPage, limit = params.loadSize,

            search = search,
            city = filterState?.location,
            gender = gender,
            maxAge = filterState?.ageRange?.endInclusive?.toInt(),
            isOnline = filterState?.isOnline,
            minRating = filterState?.minimumRating?.toFloatOrNull(),
            interests = filterState?.interests?.filter { it.isSelected }?.map { it._id },
        ).first()

        return when(result){
            is NetworkResult.Success -> {
                val chatList = result.data?.data ?: emptyList()
                val isLastPage = chatList.isEmpty() ?: false

                LoadResult.Page(
                    data = chatList,
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