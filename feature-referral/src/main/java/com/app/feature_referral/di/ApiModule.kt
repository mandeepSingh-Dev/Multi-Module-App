package com.app.feature_referral.di

import com.app.feature_referral.domain.repository.ReferralRepository
import com.app.feature_referral.remote.ReferralRepositoryImpl
import com.app.feature_referral.remote.ReferralService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideReferralService(retrofit : Retrofit) : ReferralService{
        return retrofit.create(ReferralService::class.java)
    }
    @Provides
    fun provideReferralRepository(referralRepositoryImpl : ReferralRepositoryImpl) : ReferralRepository = referralRepositoryImpl

}