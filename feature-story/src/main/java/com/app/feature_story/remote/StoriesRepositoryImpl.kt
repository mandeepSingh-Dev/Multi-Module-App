package com.app.feature_story.remote

import com.app.feature_story.domain.repository.StoriesRepository
import com.app.feature_story.remote.model.response.StoriesListResponse
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoriesRepositoryImpl @Inject constructor(private val storiesService: StoriesService,
                                                private val networkHelper: NetworkHelper) : StoriesRepository{

    override fun getStories(): Flow<NetworkResult<StoriesListResponse>> {
        return try {
            networkHelper.executeWithFlow<StoriesListResponse>(
                call = {
                    storiesService.getStories()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}