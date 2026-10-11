package com.app.feature_story.domain

import com.app.feature_story.domain.repository.StoriesRepository
import com.app.feature_story.remote.model.response.StoriesListResponse
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class StoriesAllUseCases @Inject constructor(
    val getStoriesUseCase: GetStoriesUseCase,
)
class GetStoriesUseCase @Inject constructor(private val storiesRepository: StoriesRepository) {
    operator fun invoke(): Flow<NetworkResult<StoriesListResponse>> {
        return storiesRepository.getStories()
    }
}
