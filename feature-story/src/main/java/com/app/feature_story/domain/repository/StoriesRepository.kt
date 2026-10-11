package com.app.feature_story.domain.repository

import com.app.feature_story.remote.model.response.StoriesListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface StoriesRepository {

    fun getStories(): Flow<NetworkResult<StoriesListResponse>>
}