package com.app.core_story.remote

import android.util.Log
import com.app.core_story.domain.repository.StoryRepository
import com.app.core_story.remote.model.StoryRequestBody
import com.app.core_story.remote.model.response.StoriesListResponse
import com.uae.core_network.models.ApiResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.models.ApiResponse3
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class StoryRepositoryImpl @Inject constructor(
    private val storyService: StoryService,
    private val networkHelper: NetworkHelper,
) : StoryRepository {
    override fun getStoriesList(

    ): Flow<NetworkResult<StoriesListResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<StoriesListResponse>(
                call = {
                    storyService.getStories()
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
    override fun postStory(storyRequestBody: List<StoryRequestBody.StoryRequestBodyItem>?): Flow<NetworkResult<ApiResponse3>> {
        Log.d("fkbfjkbnf", storyRequestBody.toString())
        return try {
            networkHelper.executeWithResponseManipulate<ApiResponse3>(
                call = {
                    storyService.postStory(storyRequestBody = storyRequestBody)
                },
                onSuccess = { response ->
                    response
                },
                onError = {
                    Log.d("fkbmnfkb", it.toString())
                },
            )
        } catch (e: Exception) {
            Log.d("fkbmnfkb", e.message.toString())
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}