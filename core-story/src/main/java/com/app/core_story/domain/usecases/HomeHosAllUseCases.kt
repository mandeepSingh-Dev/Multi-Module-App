package com.app.core_story.domain.usecases

import com.app.core_story.domain.repository.StoryRepository
import com.app.core_story.remote.model.StoryRequestBody
import com.app.core_story.remote.model.response.StoriesListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.models.ApiResponse3
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class StoryAllUseCases @Inject constructor(
    val getStoriesUseCase : GetStoriesUseCase,
    val postStoriesUseCase : PostStoriesUseCase,
)

class GetStoriesUseCase @Inject constructor(private val storyRepository: StoryRepository) {
    operator fun invoke(): Flow<NetworkResult<StoriesListResponse>> {
        return storyRepository.getStoriesList()
    }
}
class PostStoriesUseCase @Inject constructor(private val storyRepository: StoryRepository) {
    operator fun invoke(body : List<StoryRequestBody.StoryRequestBodyItem>?): Flow<NetworkResult<ApiResponse3>> {
        return storyRepository.postStory(storyRequestBody = body)
    }
}
