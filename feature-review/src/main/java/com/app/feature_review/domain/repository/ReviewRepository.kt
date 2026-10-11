package com.app.feature_review.domain.repository

import com.app.feature_review.remote.model.response.ReviewsListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviewsList(page: Int, limit: Int): Flow<NetworkResult<ReviewsListResponse>>
}
