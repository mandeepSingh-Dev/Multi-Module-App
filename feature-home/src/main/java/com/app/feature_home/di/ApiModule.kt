package com.app.feature_home.di

import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.HomeUserRepositoryImpl
import com.app.feature_home.remote.HomeUserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideHomeService(retrofit : Retrofit) : HomeUserService{
        return retrofit.create(HomeUserService::class.java)
    }
    @Provides
    fun provideHomeRepository(homeUserRepositoryImpl : HomeUserRepositoryImpl) : HomeUserRepository = homeUserRepositoryImpl

}