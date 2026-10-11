package com.app.core_story.di

import com.app.core_story.domain.repository.StoryRepository
import com.app.core_story.remote.StoryRepositoryImpl
import com.app.core_story.remote.StoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideReviewService(retrofit : Retrofit) : StoryService {
        return retrofit.create(StoryService::class.java)
    }
    @Provides
    fun provideReviewRepository(storyRepositoryImpl : StoryRepositoryImpl) : StoryRepository = storyRepositoryImpl

}