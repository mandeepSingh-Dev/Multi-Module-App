package com.app.feature_block.di

import com.app.feature_block.domain.repository.BlockRepository
import com.app.feature_block.remote.BlockRepositoryImpl
import com.app.feature_block.remote.BlockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideBlockService(retrofit : Retrofit) : BlockService {
        return retrofit.create(BlockService::class.java)
    }
    @Provides
    fun provideBlockRepository(blockRepositoryImpl : BlockRepositoryImpl) : BlockRepository = blockRepositoryImpl

}