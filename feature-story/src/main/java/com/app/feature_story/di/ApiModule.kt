package com.app.feature_story.di

import com.app.feature_story.domain.repository.StoriesRepository
import com.app.feature_story.remote.StoriesRepositoryImpl
import com.app.feature_story.remote.StoriesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideHomeService(retrofit : Retrofit) : StoriesService{
        return retrofit.create(StoriesService::class.java)
    }
    @Provides
    fun provideHomeRepository(storiesRepositoryImpl : StoriesRepositoryImpl) : StoriesRepository = storiesRepositoryImpl

}