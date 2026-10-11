package com.app.feature_review.remote

import com.app.feature_review.remote.model.response.ReviewsListResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReviewRatingService {

    @GET(EndPoints.REVIEW.REVIEW)
    suspend fun getReviewsList(
        @Query(ApiConstants.page) page : Int,
        @Query(ApiConstants.limit) limit : Int,
    ): Response<ReviewsListResponse>

}