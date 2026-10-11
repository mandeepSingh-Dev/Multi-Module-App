package com.app.feature_home_host.di

import com.app.feature_home_host.domain.repository.HomeHostRepository
import com.app.feature_home_host.remote.HomeHostRepositoryImpl
import com.app.feature_home_host.remote.HomeHostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideHomeService(retrofit : Retrofit) : HomeHostService{
        return retrofit.create(HomeHostService::class.java)
    }
    @Provides
    fun provideHomeRepository(homeHostRepositoryImpl : HomeHostRepositoryImpl) : HomeHostRepository = homeHostRepositoryImpl

}