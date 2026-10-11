package com.app.feature_story.remote

import com.app.feature_story.remote.model.response.StoriesListResponse
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.GET

interface StoriesService {
    @GET(EndPoints.STORY.STORY)
    suspend fun getStories() : Response<StoriesListResponse>
}