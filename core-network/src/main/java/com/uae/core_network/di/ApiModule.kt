package com.uae.core_network.di

import com.uae.core_network.data.repositoryImpl.CommonRepositoryImpl
import com.uae.core_network.data.service.CommonService
import com.uae.core_network.data.service.ServerKeyService
import com.uae.core_network.domain.repository.CommonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideCommonService(retrofit: Retrofit): CommonService {
        return retrofit.create(CommonService::class.java)
    }


    @Provides
    fun provideCommonRepository(commonRepositoryImpl : CommonRepositoryImpl): CommonRepository = commonRepositoryImpl

}