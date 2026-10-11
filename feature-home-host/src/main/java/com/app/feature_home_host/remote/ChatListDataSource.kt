/*
package com.app.feature_home_host.remote

import com.uae.core_network.networkUtils.NetworkResult
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.model.response.ChatListResponse
import kotlinx.coroutines.flow.first
import kotlin.collections.emptyList

class ChatListDataSource(
    private val search : String? = null,
    private val filter : String,
    private val homeUserRepository : HomeUserRepository,
) : PagingSource<Int, ChatListResponse.ChatListData.Chat>() {

    override fun getRefreshKey(state: PagingState<Int, ChatListResponse.ChatListData.Chat>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatListResponse.ChatListData.Chat> {
        val nextPage = params.key ?: 1

        val result = homeUserRepository.getChatList(
            page = nextPage, limit = params.loadSize,
            search = search,
            filter = filter
        ).first()



        return when(result){
            is NetworkResult.Success -> {
                val chatList = result.data?.data?.chats ?: emptyList()
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
}*/
