package com.app.feature_review.remote

import com.app.feature_review.domain.repository.ReviewRepository
import com.app.feature_review.remote.model.response.ReviewsListResponse
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class ReviewRepositoryImpl @Inject constructor(
    private val notificationService: ReviewRatingService,
    private val networkHelper: NetworkHelper,
) : ReviewRepository {
    override fun getReviewsList(
        page : Int,
        limit : Int,
    ): Flow<NetworkResult<ReviewsListResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<ReviewsListResponse>(
                call = {
                    notificationService.getReviewsList(
                        page = page,
                        limit = limit
                    )
                },
                onSuccess = { response ->
                    response
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}