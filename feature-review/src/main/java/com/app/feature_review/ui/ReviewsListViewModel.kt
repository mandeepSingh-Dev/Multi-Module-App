package com.app.feature_review.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.app.core_common.base.BaseViewModel
import com.app.feature_review.domain.repository.ReviewRepository
import com.app.feature_review.pagination.ReviewListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ReviewsListViewModel @Inject constructor(private val reviewRepository: ReviewRepository): BaseViewModel<ReviewListState>(
    ReviewListState()
) {

    val reviewsListPaging =  Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = {
            ReviewListDataSource(
                reviewRepository = reviewRepository,
                onGetSummary = {
                    updateState { state -> state.copy(summary = it) }
                }
            )
        }
    ).flow.cachedIn(viewModelScope)

}