package com.app.feature_wallet.di

import com.app.feature_wallet.domain.repository.WalletRepository
import com.app.feature_wallet.remote.WalletRepositoryImpl
import com.app.feature_wallet.remote.WalletService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideHomeService(retrofit : Retrofit) : WalletService {
        return retrofit.create(WalletService::class.java)
    }
    @Provides
    fun provideHomeRepository(walletRepositoryImpl : WalletRepositoryImpl) : WalletRepository = walletRepositoryImpl

}