package com.app.feature_notification.ui

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.feature_notification.domain.repository.NotificationRepository
import com.app.feature_notification.pagination.NotificationListDataSource
import com.app.feature_notification.ui.state.NotificationListState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(private val notificationRepository: NotificationRepository): BaseViewModel<NotificationListState>(
    NotificationListState()
) {


    val notificationListPaging =  Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = {
            NotificationListDataSource(
                notificationRepository = notificationRepository
            )
        }
    ).flow

}