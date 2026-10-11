package com.app.feature_chat.di

import com.app.feature_chat.domain.repository.ChatRepository
import com.app.feature_chat.remote.ChatRepositoryImpl
import com.app.feature_chat.remote.ChatService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideChatService(retrofit : Retrofit) : ChatService{
        return retrofit.create(ChatService::class.java)
    }
    @Provides
    fun provideChatRepository(chatRepositoryImpl : ChatRepositoryImpl) : ChatRepository = chatRepositoryImpl

}