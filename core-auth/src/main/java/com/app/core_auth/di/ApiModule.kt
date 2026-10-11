package com.app.core_auth.di

import com.app.core_auth.domain.repository.AuthRepository
import com.app.core_auth.remote.AuthRepositoryImpl
import com.app.core_auth.remote.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideAuthService(retrofit : Retrofit) : AuthService{
        return retrofit.create(AuthService::class.java)
    }
    @Provides
    fun provideAuthRepository(authRepositoryImpl : AuthRepositoryImpl) : AuthRepository = authRepositoryImpl

}