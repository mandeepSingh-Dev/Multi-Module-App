package com.app.core_story.remote

import com.app.core_story.remote.model.StoryRequestBody
import com.app.core_story.remote.model.response.StoriesListResponse
import com.uae.core_network.models.ApiResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.models.ApiResponse3
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface StoryService {


    @GET(EndPoints.STORY.STORY)
    suspend fun getStories(): Response<StoriesListResponse>

    @POST(EndPoints.STORY.STORY)
    suspend fun postStory(@Body storyRequestBody : List<StoryRequestBody.StoryRequestBodyItem>?) : Response<ApiResponse3>
}