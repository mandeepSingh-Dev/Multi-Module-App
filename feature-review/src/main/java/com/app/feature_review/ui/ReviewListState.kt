package com.app.feature_review.ui

import com.app.core_common.base.UIState
import com.app.feature_review.remote.model.response.ReviewsListResponse

data class ReviewListState(val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val summary : ReviewsListResponse.ReviewData.Summary? = null) : UIState