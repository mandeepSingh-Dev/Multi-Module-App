package com.app.feature_leaderboard.di

import com.app.feature_leaderboard.domain.repository.LeaderboardRepository
import com.app.feature_leaderboard.remote.LeaderboardRepositoryImpl
import com.app.feature_leaderboard.remote.LeaderboardService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideNotificationService(retrofit : Retrofit) : LeaderboardService {
        return retrofit.create(LeaderboardService::class.java)
    }
    @Provides
    fun provideNotificationRepository(leaderboardRepositoryImpl : LeaderboardRepositoryImpl) : LeaderboardRepository = leaderboardRepositoryImpl
}