package com.app.core_earning.di

import com.app.core_earning.domain.repository.EarningRepository
import com.app.core_earning.remote.EarningRepositoryImpl
import com.app.core_earning.remote.EarningService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideAuthService(retrofit : Retrofit) : EarningService{
        return retrofit.create(EarningService::class.java)
    }
    @Provides
    fun provideAuthRepository(earningRepositoryImpl : EarningRepositoryImpl) : EarningRepository = earningRepositoryImpl

}