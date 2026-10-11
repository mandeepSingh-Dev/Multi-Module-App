package com.app.feature_call.di

import com.app.feature_call.domain.repository.CallRepository
import com.app.feature_call.remote.CallRepositoryImpl
import com.app.feature_call.remote.CallService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideCallService(retrofit : Retrofit) : CallService{
        return retrofit.create(CallService::class.java)
    }
    @Provides
    fun provideCallRepository(callRepositoryImpl : CallRepositoryImpl) : CallRepository = callRepositoryImpl

}