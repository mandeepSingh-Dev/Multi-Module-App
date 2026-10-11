package com.app.feature_gift.di

import com.app.feature_gift.domain.repository.GiftRepository
import com.app.feature_gift.remote.GiftRepositoryImpl
import com.app.feature_gift.remote.GiftService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideHomeService(retrofit : Retrofit) : GiftService {
        return retrofit.create(GiftService::class.java)
    }
    @Provides
    fun provideHomeRepository(giftRepositoryImpl : GiftRepositoryImpl) : GiftRepository = giftRepositoryImpl

}