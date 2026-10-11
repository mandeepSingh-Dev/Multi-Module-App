package com.app.core_story.domain.repository

import com.app.core_story.remote.model.StoryRequestBody
import com.app.core_story.remote.model.response.StoriesListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.models.ApiResponse3
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getStoriesList( ): Flow<NetworkResult<StoriesListResponse>>
    fun postStory(storyRequestBody: List<StoryRequestBody.StoryRequestBodyItem>?): Flow<NetworkResult<ApiResponse3>>
}
