package com.app.feature_withdraw.di

import com.app.feature_withdraw.domain.repository.WithdrawRepository
import com.app.feature_withdraw.remote.WithdrawRepositoryImpl
import com.app.feature_withdraw.remote.WithdrawService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideWithdrawService(retrofit : Retrofit) : WithdrawService{
        return retrofit.create(WithdrawService::class.java)
    }
    @Provides
    fun provideWithdrawRepository(withdrawRepositoryImpl : WithdrawRepositoryImpl) : WithdrawRepository = withdrawRepositoryImpl

}