package com.app.feature_notification.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_notification.domain.repository.NotificationRepository
import com.app.feature_notification.remote.model.NotificationListResponse
import com.uae.core_network.domain.repository.CommonRepository
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class NotificationListDataSource(
    private val notificationRepository: NotificationRepository,
) : PagingSource<Int, NotificationListResponse.NotificationData>() {

    override fun getRefreshKey(state: PagingState<Int, NotificationListResponse.NotificationData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationListResponse.NotificationData> {
        val nextPage = params.key ?: 1


        val result = notificationRepository.getNotificationsList(
            page = nextPage, limit = params.loadSize,
        ).first()



        return when (result) {
            is NetworkResult.Success -> {
                val notificationsList = result.data?.data ?: emptyList()
                val isLastPage = notificationsList.isEmpty() ?: false

                LoadResult.Page(
                    data = notificationsList,
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